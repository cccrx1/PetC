package com.pethealth.system.service;

import java.util.Map;

public interface BehaviorTrainingService {
    Map<String, Object> generateTrainingPlan(String breed, int age, String behaviorIssue);
    Map<String, Object> getBehaviorCorrectionPlan(String behaviorIssue);
}