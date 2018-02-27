package com.appdy.metricloader.service;

import com.appdy.metricloader.client.ControllerRestClient;
import com.appdy.metricloader.dto.Application;
import com.appdy.metricloader.dto.Controller;
import com.appdy.metricloader.vo.ApiTime;
import com.appdy.metricloader.vo.TimeTaken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationHome;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MetricLoaderHelper {
    public static final Logger logger = LoggerFactory.getLogger(MetricLoaderHelper.class);
    private final File destDir;
    private final ExecutorService fileWriterPool;

    private EntityLoaderService entityLoaderService;
    private ControllerRestClient restClient;
    private ObjectMapper objectMapper;

    private ListMultimap<String, ApiTime> apiTimeMap;
    private final File metricsDir;

    public MetricLoaderHelper(EntityLoaderService entityLoaderService, ControllerRestClient restClient, ObjectMapper objectMapper) {
        this.entityLoaderService = entityLoaderService;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.apiTimeMap = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
        ApplicationHome home = new ApplicationHome(EntityLoaderService.class);
        destDir = new File(home.getDir(), "metric-loader-data");
        metricsDir = new File(destDir, "metrics");
        if (!metricsDir.exists()) {
            metricsDir.mkdirs();
        }
        fileWriterPool = Executors.newFixedThreadPool(1);
    }

    public void run() {
        List<String> baseMetricPaths = getMetricPaths();
        logger.info("The metrics paths are {}", baseMetricPaths);
        Map<String, Application[]> entityCache = entityLoaderService.getEntityCache();
        long start = System.currentTimeMillis();
        entityCache.entrySet()
                .forEach(e -> fetchMetrics(e, baseMetricPaths));

        TimeTaken timeTaken = new TimeTaken(apiTimeMap.asMap(), (System.currentTimeMillis() - start));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        File file = new File(destDir, sdf.format(new Date()) + ".json");
        try {
            objectMapper.writeValue(file, timeTaken);
        } catch (IOException e) {
            logger.error("Exception while writing teh stats to a file " + file.getAbsolutePath(), e);
        }

        try {
            fileWriterPool.shutdown();
            fileWriterPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    private List<String> getMetricPaths() {
        String metricsYml = System.getProperty("metrics.yml.path");
        InputStream in = null;
        if (StringUtils.hasText(metricsYml)) {
            File file = new File(metricsYml);
            if (file.exists()) {
                try {
                    in = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (in == null) {
            File dir = new ApplicationHome(MetricLoaderHelper.class).getDir();
            File file = new File(dir, "metrics.yml");
            if (file.exists()) {
                try {
                    in = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (in == null) {
            in = getClass().getResourceAsStream("/metrics.yml");
        }
        return (List<String>) new Yaml().load(in);
    }

    private void fetchMetrics(Map.Entry<String, Application[]> entry, List<String> baseMetricPaths) {
        String controllerUrl = entry.getKey();
        Optional<Controller> optional = entityLoaderService.getControllers()
                .stream()
                .filter(c -> Objects.equals(c.getUrl(), controllerUrl))
                .findFirst();
        if (!optional.isPresent()) {
            logger.error("Unknown controller {}", controllerUrl);
            return;
        }
        Controller controller = optional.get();
        Application[] applications = entry.getValue();
        Arrays.stream(applications)
                .parallel()
                .forEach(app -> fetchMetrics(controller, app, baseMetricPaths));
        apiTimeMap.keySet()
                .forEach(e -> printTimes(e));
    }

    private void printTimes(String appName) {
        System.out.println(appName);
        apiTimeMap.get(appName)
                .forEach(at -> System.out.println("\t" + at.getApi() + ": " + at.getTimeTaken()));
    }

    private void fetchMetrics(Controller controller, Application app, List<String> baseMetricPaths) {
        baseMetricPaths
                .forEach(p -> fetchMetrics(controller, app, p));
    }

    private String fetchMetrics(Controller controller, Application app, String path) {
        long start = System.currentTimeMillis();
        String url = UriComponentsBuilder.fromHttpUrl(controller.getUrl()).path("/controller/rest/applications/")
                .path(String.valueOf(app.getId())).path("/metric-data")
                .queryParam("metric-path", path)
                .queryParam("duration-in-mins", 5)
                .queryParam("time-range-type", "BEFORE_NOW")
                .queryParam("output", "JSON")
                .queryParam("rollup", "false")
                .build(false)
                .toUriString();
        try {
            String metrics = restClient.getJson(url, String.class, controller);
            fileWriterPool.submit(() -> writeFile(path, app, metrics));
            return metrics;
        } catch (Exception e) {
            logger.error("Exception while invoking the API " + url, e);
            apiTimeMap.put(app.getName(), new ApiTime(path, System.currentTimeMillis() - start,
                    e.getMessage()));
            return null;
        } finally {
            long timeTaken = System.currentTimeMillis() - start;
            logger.info("The time taken for {} is {}", path, timeTaken);
            apiTimeMap.put(app.getName(), new ApiTime(path, timeTaken));

        }
    }

    private void writeFile(String path, Application app, String metrics) {
        String s = path.replaceAll("\\W", "_");
        File file = new File(metricsDir, app.getName() + "-" + s + ".json");
        try {
            FileUtils.write(file, metrics);
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
