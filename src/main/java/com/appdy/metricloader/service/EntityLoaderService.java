package com.appdy.metricloader.service;

import com.appdy.metricloader.client.ControllerRestClient;
import com.appdy.metricloader.dto.Application;
import com.appdy.metricloader.dto.Controller;
import com.appdy.metricloader.dto.JsonEntityMap;
import com.appdy.metricloader.dto.Tier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EntityLoaderService {
    public static final Logger logger = LoggerFactory.getLogger(EntityLoaderService.class);

    @Autowired
    private ControllerRestClient restClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${entity-loader.enabled:false}")
    private boolean enabled;

    private Map<String, Application[]> entityCache;
    private File homeDir;
    private File destDir;
    private List<Controller> controllers;

    @PostConstruct
    public void init() {
        ApplicationHome home = new ApplicationHome(EntityLoaderService.class);
        homeDir = home.getDir();
        destDir = new File(homeDir, "metric-loader-data");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    private void fetchEntities() {
        List<Controller> controllers = getControllers();
        controllers.parallelStream()
                .map(this::fetchEntities)
                .collect(Collectors.toList());

    }

    private Application[] fetchEntities(Controller controller) {
        String url = UriComponentsBuilder.fromHttpUrl(controller.getUrl())
                .path("/controller/rest/applications")
                .queryParam("output", "JSON")
                .toUriString();
        Application[] applications = restClient.getJson(url, Application[].class, controller);
        if (applications != null) {
            Arrays.stream(applications)
                    .parallel()
                    .map(app -> fetchTiers(app, controller))
                    .collect(Collectors.toList());
        }
        File file = new File(destDir, controller.getAccount() + "-entities.json");
        try {
            logger.info("Writing the entities file at {}", file.getAbsolutePath());
            JsonEntityMap map = new JsonEntityMap();
            map.setApplications(applications);
            map.setController(controller.getUrl());
            map.setTime(System.currentTimeMillis());
            objectMapper.writeValue(file, map);
        } catch (IOException e) {
            logger.error("Exception while creating the entities file at " + file.getAbsolutePath(), e);
        }
        return applications;
    }

    private Application fetchTiers(Application app, Controller controller) {
        String url = UriComponentsBuilder.fromHttpUrl(controller.getUrl())
                .path("/controller/rest/applications/").path(String.valueOf(app.getId()))
                .path("/tiers")
                .queryParam("output", "JSON")
                .toUriString();
        Tier[] tiers = restClient.getJson(url, Tier[].class, controller);
        if (tiers != null) {
            app.setTiers(Arrays.asList(tiers));
        }
        return app;
    }


    public List<Controller> getControllers() {
        if (controllers == null) {
            controllers = doGetControllers();
        }
        return controllers;
    }

    private List<Controller> doGetControllers() {
        File file = getControllersFile();
        try (InputStream in = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Controller[] controllers = yaml.loadAs(in, Controller[].class);
            return Arrays.asList(controllers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File getControllersFile() {
        String propPath = System.getProperty("controllers.yml.path");
        if (StringUtils.hasText(propPath)) {
            File file = new File(propPath);
            if (file.exists()) {
                logger.info("Loading the file from the system property path {}", file.getAbsolutePath());
                return file;
            } else {
                logger.error("The controllers.yml doesn't exist at the path {}", file.getAbsolutePath());
            }
        }
        File file = new File(homeDir, "controllers.yml");
        if (file.exists()) {
            logger.info("Loading the controllers from {}", file.getAbsolutePath());
            return file;
        } else {
            throw new RuntimeException("Cannot locate the controllers.yml");
        }
    }

    public Map<String, Application[]> getEntityCache() {
        if (entityCache == null) {
            entityCache = loadEntities();
        }
        return entityCache;
    }

    private Map<String, Application[]> loadEntities() {
        Map<String, Application[]> entityCache = new HashMap<>();
        File[] files = destDir.listFiles((dir, name) -> name.endsWith("-entities.json"));
        if (files != null) {
            for (File file : files) {
                try {
                    JsonEntityMap map = objectMapper.readValue(file, JsonEntityMap.class);
                    entityCache.put(map.getController(), map.getApplications());
                } catch (IOException e) {
                    logger.error("Error while converting teh file to json " + file.getAbsolutePath(), e);
                }
            }
        }
        List<Controller> controllers = getControllers();
        for (Controller controller : controllers) {
            if (!entityCache.containsKey(controller.getUrl())) {
                Application[] applications = fetchEntities(controller);
                entityCache.put(controller.getUrl(), applications);
            }
        }
        return entityCache;
    }
}
