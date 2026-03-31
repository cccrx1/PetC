package com.pethealth.system.service;

public interface Text2SQLService {
    String executeQuery(String naturalLanguage, Long userId);
}