package com.pethealth.system.service.impl;

import com.pethealth.system.service.ChatAssistantService;
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

    @Value("${chat-assistant.api.url}")
    private String chatApiUrl;

    @Value("${chat-assistant.api.key}")
    private String chatApiKey;

    @Value("${chat-assistant.api.app-id}")
    private String chatAppId;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> sendMessage(String message, String userId, String conversationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + chatApiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("app_id", chatAppId);
        requestBody.put("user", userId);
        requestBody.put("query", message);
        if (conversationId != null && !conversationId.isEmpty()) {
            requestBody.put("conversation_id", conversationId);
        }

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                chatApiUrl, HttpMethod.POST, requestEntity, Map.class);

        return response.getBody();
    }

    @Override
    public List<Map<String, Object>> getConversationHistory(String userId, String conversationId) {
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
    }
}
