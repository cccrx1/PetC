package com.pethealth.system.service.impl;

import com.pethealth.system.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Value("${chat-assistant.api.url}")
    private String apiUrl;

    @Value("${chat-assistant.api.key}")
    private String apiKey;

    @Value("${chat-assistant.api.app-id}")
    private String appId;

    private final RestTemplate restTemplate;

    public ChatServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Map<String, Object> sendMessage(String message) {
        logger.info("Sending message to Dify: message={}", message);
        logger.info("Chat API URL: {}", apiUrl);
        logger.info("Chat API Key: {}", apiKey);
        logger.info("Chat App ID: {}", appId);
        
        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("app_id", appId);
            requestBody.put("user", "1"); // 暂时使用固定用户ID
            requestBody.put("query", message);
            
            // 添加inputs字段，Dify API要求必须包含
            Map<String, Object> inputs = new HashMap<>();
            inputs.put("message", message);
            requestBody.put("inputs", inputs);

            logger.info("Request body: {}", requestBody);

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

            logger.info("Response status: {}", response.getStatusCode());
            logger.info("Response body: {}", response.getBody());

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error sending message to Dify: {}", e.getMessage(), e);
            throw e;
        }
    }
}