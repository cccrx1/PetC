package com.pethealth.system.controller;

import com.pethealth.system.service.ChatAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatAssistantController {

    @Autowired
    private ChatAssistantService chatAssistantService;

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @RequestParam String message,
            @RequestParam String userId,
            @RequestParam(required = false) String conversationId) {
        Map<String, Object> response = chatAssistantService.sendMessage(message, userId, conversationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Map<String, Object>>> getConversationHistory(
            @RequestParam String userId,
            @RequestParam String conversationId) {
        List<Map<String, Object>> history = chatAssistantService.getConversationHistory(userId, conversationId);
        return ResponseEntity.ok(history);
    }
}
