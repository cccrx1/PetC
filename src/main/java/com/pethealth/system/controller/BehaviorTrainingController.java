package com.pethealth.system.controller;

import com.pethealth.system.service.BehaviorTrainingService;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/behavior-training")
public class BehaviorTrainingController {
    
    @Autowired
    private BehaviorTrainingService behaviorTrainingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private Long getUserIdFromToken(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        return userService.getProfile(username).getId();
    }
    
    @PostMapping("/training-plan")
    public ResponseEntity<Map<String, Object>> generateTrainingPlan(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> request) {
        
        getUserIdFromToken(token); // 验证用户身份
        
        String breed = (String) request.get("breed");
        int age = (int) request.get("age");
        String behaviorIssue = (String) request.get("behaviorIssue");
        
        Map<String, Object> trainingPlan = behaviorTrainingService.generateTrainingPlan(breed, age, behaviorIssue);
        return ResponseEntity.ok(trainingPlan);
    }
    
    @PostMapping("/behavior-correction")
    public ResponseEntity<Map<String, Object>> getBehaviorCorrectionPlan(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> request) {
        
        getUserIdFromToken(token); // 验证用户身份
        
        String behaviorIssue = (String) request.get("behaviorIssue");
        
        Map<String, Object> correctionPlan = behaviorTrainingService.getBehaviorCorrectionPlan(behaviorIssue);
        return ResponseEntity.ok(correctionPlan);
    }
}