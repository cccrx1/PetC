package com.pethealth.system.service.impl;

import com.pethealth.system.service.BehaviorTrainingService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BehaviorTrainingServiceImpl implements BehaviorTrainingService {

    @Override
    public Map<String, Object> generateTrainingPlan(String breed, int age, String behaviorIssue) {
        Map<String, Object> plan = new HashMap<>();
        plan.put("breed", breed);
        plan.put("age", age);
        plan.put("behaviorIssue", behaviorIssue);
        plan.put("generatedAt", new Date());

        List<Map<String, Object>> weeklyPlan = new ArrayList<>();
        
        // 周一训练计划
        Map<String, Object> monday = new HashMap<>();
        monday.put("day", "Monday");
        monday.put("activities", Arrays.asList(
            "基础服从训练: 坐下指令练习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "互动游戏: Fetch 游戏 15分钟"
        ));
        weeklyPlan.add(monday);

        // 周二训练计划
        Map<String, Object> tuesday = new HashMap<>();
        tuesday.put("day", "Tuesday");
        tuesday.put("activities", Arrays.asList(
            "基础服从训练: 趴下指令练习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "社交训练: 与其他宠物互动 20分钟"
        ));
        weeklyPlan.add(tuesday);

        // 周三训练计划
        Map<String, Object> wednesday = new HashMap<>();
        wednesday.put("day", "Wednesday");
        wednesday.put("activities", Arrays.asList(
            "基础服从训练: 待命指令练习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "体能训练: 散步 30分钟"
        ));
        weeklyPlan.add(wednesday);

        // 周四训练计划
        Map<String, Object> thursday = new HashMap<>();
        thursday.put("day", "Thursday");
        thursday.put("activities", Arrays.asList(
            "基础服从训练: 召回指令练习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "智力游戏: 益智玩具 15分钟"
        ));
        weeklyPlan.add(thursday);

        // 周五训练计划
        Map<String, Object> friday = new HashMap<>();
        friday.put("day", "Friday");
        friday.put("activities", Arrays.asList(
            "综合训练: 指令串联练习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "放松时间: 按摩和梳理 10分钟"
        ));
        weeklyPlan.add(friday);

        // 周六训练计划
        Map<String, Object> saturday = new HashMap<>();
        saturday.put("day", "Saturday");
        saturday.put("activities", Arrays.asList(
            "户外训练: 公共场合服从练习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "长时间活动: 公园游玩 1小时"
        ));
        weeklyPlan.add(saturday);

        // 周日训练计划
        Map<String, Object> sunday = new HashMap<>();
        sunday.put("day", "Sunday");
        sunday.put("activities", Arrays.asList(
            "复习训练: 本周所学指令复习",
            "行为纠正: " + getBehaviorCorrectionActivity(behaviorIssue),
            "休息时间: 自由活动"
        ));
        weeklyPlan.add(sunday);

        plan.put("weeklyPlan", weeklyPlan);
        plan.put("tips", getTrainingTips(breed, age, behaviorIssue));

        return plan;
    }

    @Override
    public Map<String, Object> getBehaviorCorrectionPlan(String behaviorIssue) {
        Map<String, Object> correctionPlan = new HashMap<>();
        correctionPlan.put("behaviorIssue", behaviorIssue);
        correctionPlan.put("generatedAt", new Date());

        switch (behaviorIssue.toLowerCase()) {
            case "aggression":
                correctionPlan.put("description", "攻击性行为纠正方案");
                correctionPlan.put("steps", Arrays.asList(
                    "识别触发因素并避免",
                    "建立明确的等级制度",
                    "使用正向强化训练",
                    "逐渐脱敏训练",
                    "寻求专业训练师帮助"
                ));
                correctionPlan.put("tips", "保持冷静，避免惩罚，使用奖励系统");
                break;
            case "excessive barking":
                correctionPlan.put("description", "过度吠叫纠正方案");
                correctionPlan.put("steps", Arrays.asList(
                    "识别吠叫原因",
                    "提供足够的运动和刺激",
                    "训练安静指令",
                    "避免强化吠叫行为",
                    "使用分散注意力的方法"
                ));
                correctionPlan.put("tips", "当宠物安静时给予奖励，保持一致性");
                break;
            case "separation anxiety":
                correctionPlan.put("description", "分离焦虑纠正方案");
                correctionPlan.put("steps", Arrays.asList(
                    "逐渐适应独处时间",
                    "创建安全的环境",
                    "使用安抚物品",
                    "建立离开和返回的规律",
                    "考虑药物辅助（在兽医指导下）"
                ));
                correctionPlan.put("tips", "保持离开和返回的平静，避免过度安抚");
                break;
            case "destructive behavior":
                correctionPlan.put("description", "破坏性行为纠正方案");
                correctionPlan.put("steps", Arrays.asList(
                    "提供适当的咀嚼玩具",
                    "增加运动量和 mental stimulation",
                    "限制可破坏的区域",
                    "使用正向强化训练",
                    "监督和重定向行为"
                ));
                correctionPlan.put("tips", "当宠物使用正确物品时给予奖励");
                break;
            case "house soiling":
                correctionPlan.put("description", "随地大小便纠正方案");
                correctionPlan.put("steps", Arrays.asList(
                    "建立规律的排泄时间表",
                    "使用一致的厕所区域",
                    "监督和及时奖励正确行为",
                    "彻底清洁之前的事故区域",
                    "排除健康问题（如尿路感染）"
                ));
                correctionPlan.put("tips", "保持耐心，避免惩罚，使用酶清洁剂");
                break;
            default:
                correctionPlan.put("description", "一般行为纠正方案");
                correctionPlan.put("steps", Arrays.asList(
                    "识别行为触发因素",
                    "建立一致的训练计划",
                    "使用正向强化",
                    "保持耐心和一致性",
                    "考虑寻求专业帮助"
                ));
                correctionPlan.put("tips", "每个宠物都是独特的，根据具体情况调整方案");
        }

        return correctionPlan;
    }

    private String getBehaviorCorrectionActivity(String behaviorIssue) {
        switch (behaviorIssue.toLowerCase()) {
            case "aggression":
                return "脱敏训练：逐渐接触触发因素，每次成功后给予奖励";
            case "excessive barking":
                return "安静指令训练：当宠物停止吠叫时给予奖励";
            case "separation anxiety":
                return "独处训练：逐渐延长独处时间，每次成功后给予奖励";
            case "destructive behavior":
                return "重定向训练：将破坏行为引导到适当的玩具上";
            case "house soiling":
                return "厕所训练：建立规律的排泄时间表，及时奖励";
            default:
                return "行为纠正训练：根据具体问题进行针对性练习";
        }
    }

    private List<String> getTrainingTips(String breed, int age, String behaviorIssue) {
        List<String> tips = new ArrayList<>();
        tips.add("保持训练 sessions 短而频繁（10-15分钟）");
        tips.add("使用高价值的奖励来强化良好行为");
        tips.add("保持训练的一致性和耐心");
        tips.add("避免惩罚，使用正向强化");
        
        // 根据年龄添加特定建议
        if (age < 1) {
            tips.add("幼犬训练应注重社会化和基础指令");
            tips.add("保持训练游戏化，适合幼犬注意力跨度");
        } else if (age > 7) {
            tips.add("老年犬训练应考虑体力和认知能力");
            tips.add("使用更温和的训练方法，给予更多休息时间");
        }
        
        // 根据行为问题添加特定建议
        if (behaviorIssue.equalsIgnoreCase("aggression")) {
            tips.add("避免触发攻击性行为的情境");
            tips.add("使用专业的训练设备，如头套");
        } else if (behaviorIssue.equalsIgnoreCase("separation anxiety")) {
            tips.add("离开前提供有趣的玩具或食物");
            tips.add("逐渐增加独处时间，不要突然离开");
        }
        
        return tips;
    }
}