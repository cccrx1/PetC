package com.pethealth.system.service;

import java.util.List;
import java.util.Map;

public interface ChatAssistantService {
    Map<String, Object> sendMessage(String message, String userId, String conversationId);
    List<Map<String, Object>> getConversationHistory(String userId, String conversationId);
}
