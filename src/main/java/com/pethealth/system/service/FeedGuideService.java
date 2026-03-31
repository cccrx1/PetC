package com.pethealth.system.service;

import java.util.Map;

public interface FeedGuideService {
    /**
     * 计算宠物每日热量需求
     * @param breed 宠物品种
     * @param age 宠物年龄
     * @param weight 宠物体重
     * @param gender 宠物性别
     * @return 每日热量需求（千卡）
     */
    double calculateDailyCalories(String breed, int age, double weight, String gender);
    
    /**
     * 生成个性化食谱建议
     * @param breed 宠物品种
     * @param age 宠物年龄
     * @param weight 宠物体重
     * @param gender 宠物性别
     * @return 食谱建议
     */
    Map<String, Object> generateDietRecommendation(String breed, int age, double weight, String gender);
    
    /**
     * 生成完整的喂养方案
     * @param petId 宠物ID
     * @return 喂养方案
     */
    Map<String, Object> generateFeedPlan(Long petId);
}