package com.pethealth.system.controller;

import com.pethealth.system.service.LifePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/life-plan")
public class LifePlanController {

    private final LifePlanService lifePlanService;

    @Autowired
    public LifePlanController(LifePlanService lifePlanService) {
        this.lifePlanService = lifePlanService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateLifePlan(@RequestBody Map<String, Object> request) {
        String breed = (String) request.get("breed");
        int age = Integer.parseInt(request.get("age").toString());
        double weight = Double.parseDouble(request.get("weight").toString());

        Map<String, Object> result = lifePlanService.generateLifePlan(breed, age, weight);
        return ResponseEntity.ok(result);
    }
}
