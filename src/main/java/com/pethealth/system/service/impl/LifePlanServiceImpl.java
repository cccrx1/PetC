package com.pethealth.system.service.impl;

import com.pethealth.system.service.LifePlanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LifePlanServiceImpl implements LifePlanService {

    @Value("${life-plan.api.url}")
    private String apiUrl;

    @Value("${life-plan.api.key}")
    private String apiKey;

    @Value("${life-plan.api.workflow-id}")
    private String workflowId;

    private final RestTemplate restTemplate;

    public LifePlanServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Map<String, Object> generateLifePlan(String breed, int age, double weight) {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("workflow_id", workflowId);
        
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("breed", breed);
        inputs.put("age", age);
        inputs.put("weight", weight);
        
        requestBody.put("inputs", inputs);
        requestBody.put("response_mode", "blocking");

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 调用Dify API
        ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        return response.getBody();
    }
}
