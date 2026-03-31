package com.pethealth.system.service;

import java.util.Map;

public interface LifePlanService {
    Map<String, Object> generateLifePlan(String breed, int age, double weight);
}
