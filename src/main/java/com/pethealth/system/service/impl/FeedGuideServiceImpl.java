package com.pethealth.system.service.impl;

import com.pethealth.system.entity.Pet;
import com.pethealth.system.repository.PetRepository;
import com.pethealth.system.service.FeedGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedGuideServiceImpl implements FeedGuideService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public double calculateDailyCalories(String breed, int age, double weight, String gender) {
        // 基础代谢率计算（根据宠物类型和体重）
        double bmr;
        
        // 简化的品种分类
        if (isSmallBreed(breed)) {
            // 小型犬/猫：每公斤体重需要更多热量
            bmr = 70 * Math.pow(weight, 0.75);
        } else if (isLargeBreed(breed)) {
            // 大型犬：每公斤体重需要较少热量
            bmr = 60 * Math.pow(weight, 0.75);
        } else {
            // 中型犬/猫：标准计算
            bmr = 65 * Math.pow(weight, 0.75);
        }
        
        // 年龄调整系数
        double ageFactor;
        if (age < 1) {
            // 幼年期：需要更多热量
            ageFactor = 2.0;
        } else if (age < 7) {
            // 成年期：标准热量
            ageFactor = 1.0;
        } else {
            // 老年期：需要较少热量
            ageFactor = 0.8;
        }
        
        // 活动水平调整（这里使用默认值，实际应用中可以根据宠物活动情况调整）
        double activityFactor = 1.2;
        
        // 计算总热量
        return bmr * ageFactor * activityFactor;
    }

    @Override
    public Map<String, Object> generateDietRecommendation(String breed, int age, double weight, String gender) {
        Map<String, Object> recommendation = new HashMap<>();
        
        // 计算每日热量需求
        double dailyCalories = calculateDailyCalories(breed, age, weight, gender);
        recommendation.put("dailyCalories", dailyCalories);
        
        // 营养比例建议
        Map<String, Double> nutrientRatios = new HashMap<>();
        if (age < 1) {
            // 幼年期：更高的蛋白质和脂肪
            nutrientRatios.put("protein", 0.30); // 30%
            nutrientRatios.put("fat", 0.25);    // 25%
            nutrientRatios.put("carbs", 0.45);   // 45%
        } else if (age < 7) {
            // 成年期：标准营养比例
            nutrientRatios.put("protein", 0.25); // 25%
            nutrientRatios.put("fat", 0.20);    // 20%
            nutrientRatios.put("carbs", 0.55);   // 55%
        } else {
            // 老年期：更高的蛋白质，更低的脂肪
            nutrientRatios.put("protein", 0.30); // 30%
            nutrientRatios.put("fat", 0.15);    // 15%
            nutrientRatios.put("carbs", 0.55);   // 55%
        }
        recommendation.put("nutrientRatios", nutrientRatios);
        
        // 食物建议
        List<String> foodRecommendations = new ArrayList<>();
        if (isDogBreed(breed)) {
            if (age < 1) {
                foodRecommendations.add("幼犬专用粮，富含蛋白质和钙质");
                foodRecommendations.add("适量添加鸡胸肉、鱼肉等优质蛋白");
                foodRecommendations.add("补充维生素和矿物质");
            } else if (age < 7) {
                foodRecommendations.add("成犬专用粮，均衡营养");
                foodRecommendations.add("适量添加新鲜蔬菜和水果");
                foodRecommendations.add("定期补充Omega-3脂肪酸");
            } else {
                foodRecommendations.add("老年犬专用粮，易消化");
                foodRecommendations.add("控制热量摄入，避免肥胖");
                foodRecommendations.add("补充关节保健成分");
            }
        } else if (isCatBreed(breed)) {
            if (age < 1) {
                foodRecommendations.add("幼猫专用粮，高蛋白质");
                foodRecommendations.add("适量添加牛磺酸补充剂");
                foodRecommendations.add("保证充足的水分摄入");
            } else if (age < 7) {
                foodRecommendations.add("成猫专用粮，均衡营养");
                foodRecommendations.add("适量添加猫草，帮助消化");
                foodRecommendations.add("定期喂食化毛膏");
            } else {
                foodRecommendations.add("老年猫专用粮，易消化");
                foodRecommendations.add("控制磷含量，保护肾脏");
                foodRecommendations.add("补充维生素E和C，增强免疫力");
            }
        } else {
            foodRecommendations.add("根据宠物类型选择合适的专业粮");
            foodRecommendations.add("保证饮食均衡，营养全面");
            foodRecommendations.add("定期检查宠物健康状况");
        }
        recommendation.put("foodRecommendations", foodRecommendations);
        
        // 喂养频率建议
        Map<String, Integer> feedingFrequency = new HashMap<>();
        if (age < 1) {
            feedingFrequency.put("dailyMeals", 4);
            feedingFrequency.put("portionSize", (int) (dailyCalories / 4));
        } else if (age < 7) {
            feedingFrequency.put("dailyMeals", 2);
            feedingFrequency.put("portionSize", (int) (dailyCalories / 2));
        } else {
            feedingFrequency.put("dailyMeals", 3);
            feedingFrequency.put("portionSize", (int) (dailyCalories / 3));
        }
        recommendation.put("feedingFrequency", feedingFrequency);
        
        return recommendation;
    }

    @Override
    public Map<String, Object> generateFeedPlan(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        
        Map<String, Object> feedPlan = new HashMap<>();
        feedPlan.put("petName", pet.getName());
        feedPlan.put("breed", pet.getBreed());
        feedPlan.put("age", pet.getAge());
        feedPlan.put("weight", pet.getWeight());
        feedPlan.put("gender", pet.getGender());
        
        // 生成食谱建议
        Map<String, Object> dietRecommendation = generateDietRecommendation(
                pet.getBreed(), pet.getAge(), pet.getWeight(), pet.getGender());
        feedPlan.put("dietRecommendation", dietRecommendation);
        
        // 动态调整建议
        List<String> adjustmentSuggestions = new ArrayList<>();
        if (isOverweight(pet.getBreed(), pet.getWeight())) {
            adjustmentSuggestions.add("减少每日热量摄入10-15%");
            adjustmentSuggestions.add("增加运动量");
            adjustmentSuggestions.add("选择低热量、高纤维的食物");
        } else if (isUnderweight(pet.getBreed(), pet.getWeight())) {
            adjustmentSuggestions.add("增加每日热量摄入10-15%");
            adjustmentSuggestions.add("选择高蛋白质、高热量的食物");
            adjustmentSuggestions.add("增加喂食次数");
        } else {
            adjustmentSuggestions.add("保持当前饮食结构");
            adjustmentSuggestions.add("定期监测体重变化");
            adjustmentSuggestions.add("根据季节和活动量适当调整");
        }
        feedPlan.put("adjustmentSuggestions", adjustmentSuggestions);
        
        return feedPlan;
    }

    // 辅助方法：判断是否为小型品种
    private boolean isSmallBreed(String breed) {
        String[] smallBreeds = {"吉娃娃", "约克夏", "博美", "比熊", "泰迪", "柯基", "小型犬", "小猫"};
        for (String smallBreed : smallBreeds) {
            if (breed.contains(smallBreed)) {
                return true;
            }
        }
        return false;
    }

    // 辅助方法：判断是否为大型品种
    private boolean isLargeBreed(String breed) {
        String[] largeBreeds = {"金毛", "拉布拉多", "德牧", "阿拉斯加", "萨摩耶", "大型犬", "大猫"};
        for (String largeBreed : largeBreeds) {
            if (breed.contains(largeBreed)) {
                return true;
            }
        }
        return false;
    }

    // 辅助方法：判断是否为犬类品种
    private boolean isDogBreed(String breed) {
        String[] dogBreeds = {"犬", "狗", "金毛", "拉布拉多", "德牧", "阿拉斯加", "萨摩耶", "吉娃娃", "约克夏", "博美", "比熊", "泰迪", "柯基"};
        for (String dogBreed : dogBreeds) {
            if (breed.contains(dogBreed)) {
                return true;
            }
        }
        return false;
    }

    // 辅助方法：判断是否为猫类品种
    private boolean isCatBreed(String breed) {
        String[] catBreeds = {"猫", "波斯", "英短", "美短", "布偶", "暹罗"};
        for (String catBreed : catBreeds) {
            if (breed.contains(catBreed)) {
                return true;
            }
        }
        return false;
    }

    // 辅助方法：判断是否超重
    private boolean isOverweight(String breed, double weight) {
        // 简化的判断逻辑，实际应用中应根据具体品种的标准体重范围判断
        if (isSmallBreed(breed) && weight > 10) {
            return true;
        } else if (isLargeBreed(breed) && weight > 40) {
            return true;
        } else if (!isSmallBreed(breed) && !isLargeBreed(breed) && weight > 20) {
            return true;
        }
        return false;
    }

    // 辅助方法：判断是否体重不足
    private boolean isUnderweight(String breed, double weight) {
        // 简化的判断逻辑，实际应用中应根据具体品种的标准体重范围判断
        if (isSmallBreed(breed) && weight < 2) {
            return true;
        } else if (isLargeBreed(breed) && weight < 20) {
            return true;
        } else if (!isSmallBreed(breed) && !isLargeBreed(breed) && weight < 5) {
            return true;
        }
        return false;
    }
}