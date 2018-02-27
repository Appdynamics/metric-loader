package com.appdy.metricloader.service;

import com.appdy.metricloader.client.ControllerRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricLoaderService {
    public static final Logger logger = LoggerFactory.getLogger(MetricLoaderService.class);

    @Autowired
    private EntityLoaderService entityLoaderService;
    @Autowired
    private ControllerRestClient restClient;
    @Autowired
    private ObjectMapper objectMapper;

    public void fetchMetrics() {
        logger.info("Fetching the metrics");
        MetricLoaderHelper helper = new MetricLoaderHelper(entityLoaderService, restClient, objectMapper);
        helper.run();
    }


}
