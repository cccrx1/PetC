package com.pethealth.system.service;

import java.util.Map;

public interface ChatService {
    Map<String, Object> sendMessage(String message);
}