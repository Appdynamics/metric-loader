package com.appdy.metricloader.client;

import com.appdy.metricloader.dto.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class ControllerRestClient {
    public static final Logger logger = LoggerFactory.getLogger(ControllerRestClient.class);

    @Autowired
    private RestTemplate restTemplate;

    public <T> T getJson(String url, Class<T> clazz, Controller controller) {
        logger.info("Invoking the url {}:{}", controller, url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authorization = String.format("%s@%s:%s",
                controller.getUser(), controller.getAccount(), controller.getPassword());
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString(authorization.getBytes()));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, clazz);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            String msg = String.format("The request to %s %s failed. The response is %s", controller, url, response);
            logger.error(msg);
            throw new RuntimeException(msg);

        }
    }
}
