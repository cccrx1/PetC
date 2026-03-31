package com.pethealth.system.service.impl;

import com.pethealth.system.service.Text2SQLService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class Text2SQLServiceImpl implements Text2SQLService {

    @Value("${text2sql.api.url}")
    private String apiUrl;

    @Value("${text2sql.api.key}")
    private String apiKey;

    @Value("${text2sql.api.workflow-id}")
    private String workflowId;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String executeQuery(String naturalLanguage, Long userId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("workflow_id", workflowId);
        
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("natural_language", naturalLanguage);
        inputs.put("user_id", userId);
        requestBody.put("inputs", inputs);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        org.springframework.http.HttpEntity<Map<String, Object>> entity = new org.springframework.http.HttpEntity<>(requestBody, headers);

        Map<String, Object> response = restTemplate.postForObject(
            apiUrl,
            entity,
            Map.class
        );

        if (response != null && response.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data.containsKey("output")) {
                Map<String, Object> output = (Map<String, Object>) data.get("output");
                return (String) output.get("result");
            }
        }
        return "查询失败，请重试";
    }
}