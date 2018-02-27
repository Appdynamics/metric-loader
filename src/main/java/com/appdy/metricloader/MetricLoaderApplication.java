package com.appdy.metricloader;

import com.appdy.metricloader.service.MetricLoaderService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationHome;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MetricLoaderApplication {

    public static void main(String[] args) {
        ApplicationHome home = new ApplicationHome(MetricLoaderApplication.class);
        String dir = home.getDir().getAbsolutePath();
        System.out.println("Log dir is set to " + dir);
        System.setProperty("LOG_DIR", dir);
        ConfigurableApplicationContext context = SpringApplication.run(MetricLoaderApplication.class, args);
        MetricLoaderService service = context.getBean(MetricLoaderService.class);
        service.fetchMetrics();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
