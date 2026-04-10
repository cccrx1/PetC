package com.pethealth.system.service.impl;

import com.pethealth.system.service.ChatAssistantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatAssistantServiceImpl implements ChatAssistantService {

    private static final Logger logger = LoggerFactory.getLogger(ChatAssistantServiceImpl.class);

    @Value("${chat-assistant.api.url}")
    private String chatApiUrl;

    @Value("${chat-assistant.api.key}")
    private String chatApiKey;

    @Value("${chat-assistant.api.app-id}")
    private String chatAppId;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> sendMessage(String message, String userId, String conversationId) {
        logger.info("Sending message to Dify: message={}, userId={}, conversationId={}", message, userId, conversationId);
        logger.info("Chat API URL: {}", chatApiUrl);
        // 移除API密钥的日志记录
        logger.info("Chat App ID: {}", chatAppId);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + chatApiKey);
            headers.set("Content-Type", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("app_id", chatAppId);
            requestBody.put("user", userId);
            requestBody.put("query", message);
            
            // 添加inputs字段，Dify API要求必须包含
            Map<String, Object> inputs = new HashMap<>();
            inputs.put("message", message);
            requestBody.put("inputs", inputs);
            
            if (conversationId != null && !conversationId.isEmpty()) {
                requestBody.put("conversation_id", conversationId);
            }

            logger.info("Request body: {}", requestBody);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    chatApiUrl, HttpMethod.POST, requestEntity, Map.class);

            logger.info("Response status: {}", response.getStatusCode());
            logger.info("Response body: {}", response.getBody());

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error sending message to Dify: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getConversationHistory(String userId, String conversationId) {
        logger.info("Getting conversation history from Dify: userId={}, conversationId={}", userId, conversationId);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + chatApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("app_id", chatAppId);
            requestBody.put("user", userId);
            requestBody.put("conversation_id", conversationId);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    chatApiUrl + "/history", HttpMethod.GET, requestEntity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("messages")) {
                return (List<Map<String, Object>>) responseBody.get("messages");
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error getting conversation history from Dify: {}", e.getMessage(), e);
            throw e;
        }
    }
}
