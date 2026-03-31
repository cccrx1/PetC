package com.pethealth.system.controller;

import com.pethealth.system.service.FeedGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feed-guide")
public class FeedGuideController {

    @Autowired
    private FeedGuideService feedGuideService;

    /**
     * 计算宠物每日热量需求
     * @param breed 宠物品种
     * @param age 宠物年龄
     * @param weight 宠物体重
     * @param gender 宠物性别
     * @return 每日热量需求
     */
    @GetMapping("/calculate-calories")
    public ResponseEntity<Map<String, Object>> calculateCalories(
            @RequestParam String breed,
            @RequestParam int age,
            @RequestParam double weight,
            @RequestParam String gender) {
        double calories = feedGuideService.calculateDailyCalories(breed, age, weight, gender);
        return ResponseEntity.ok(Map.of("dailyCalories", calories));
    }

    /**
     * 生成个性化食谱建议
     * @param breed 宠物品种
     * @param age 宠物年龄
     * @param weight 宠物体重
     * @param gender 宠物性别
     * @return 食谱建议
     */
    @GetMapping("/diet-recommendation")
    public ResponseEntity<Map<String, Object>> generateDietRecommendation(
            @RequestParam String breed,
            @RequestParam int age,
            @RequestParam double weight,
            @RequestParam String gender) {
        Map<String, Object> recommendation = feedGuideService.generateDietRecommendation(breed, age, weight, gender);
        return ResponseEntity.ok(recommendation);
    }

    /**
     * 根据宠物ID生成完整的喂养方案
     * @param petId 宠物ID
     * @return 喂养方案
     */
    @GetMapping("/feed-plan/{petId}")
    public ResponseEntity<Map<String, Object>> generateFeedPlan(@PathVariable Long petId) {
        Map<String, Object> feedPlan = feedGuideService.generateFeedPlan(petId);
        return ResponseEntity.ok(feedPlan);
    }
}