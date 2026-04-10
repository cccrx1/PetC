package com.pethealth.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pethealth.system.service.ConsultationWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConsultationWorkflowServiceImpl implements ConsultationWorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationWorkflowServiceImpl.class);

    // 使用用户提供的工作流Base URL
    private final String workflowUrl = "http://47.113.151.36//v1/workflows/run";

    // API Key
    private final String apiKey = "app-nLfURLT0CVbFKmaLNhYem5fu";

    // 工作流名称
    private final String workflowName = "宠物咨询";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> processConsultation(Long consultationId, String symptoms, String petBreed, int petAge, String petName) {
        logger.info("Processing consultation with workflow: id={}, symptoms={}, petBreed={}, petAge={}, petName={}", 
                consultationId, symptoms, petBreed, petAge, petName);
        
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        
        requestBody.put("workflow_name", workflowName);
        requestBody.put("user", "test456"); // 添加user参数
        
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("consultation_id", String.valueOf(consultationId)); // 将consultation_id转换为字符串类型
        inputs.put("symptoms", symptoms);
        inputs.put("pet_breed", petBreed);
        inputs.put("pet_age", String.valueOf(petAge)); // 将pet_age转换为字符串类型
        inputs.put("pet_name", petName);
        requestBody.put("inputs", inputs);
        requestBody.put("response_mode", "blocking");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 首先尝试获取原始响应
            ResponseEntity<String> stringResponse = restTemplate.exchange(
                    workflowUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            
            logger.info("Workflow response status: {}", stringResponse.getStatusCode());
            logger.info("Workflow response content type: {}", stringResponse.getHeaders().getContentType());
            
            // 检查响应是否为HTML
            String responseBody = stringResponse.getBody();
            if (responseBody != null && (responseBody.contains("<!DOCTYPE html") || responseBody.contains("<html"))) {
                logger.warn("Workflow returned HTML instead of JSON");
                // 检查是否是404页面（更严格的检查）
                if (stringResponse.getStatusCode().value() == 404 || 
                    (responseBody.toLowerCase().contains("404") && responseBody.toLowerCase().contains("not found"))) {
                    logger.error("Workflow URL returned 404 Not Found");
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Workflow URL returned 404 Not Found");
                    errorResponse.put("status", "error");
                    return errorResponse;
                } else {
                    // 尝试从HTML中提取JSON内容
                    try {
                        // 查找JSON开始和结束的位置
                        int jsonStart = responseBody.indexOf("{");
                        int jsonEnd = responseBody.lastIndexOf("}") + 1;
                        if (jsonStart != -1 && jsonEnd != -1) {
                            String jsonContent = responseBody.substring(jsonStart, jsonEnd);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Map<String, Object> parsedResponse = objectMapper.readValue(jsonContent, Map.class);
                            logger.info("Extracted JSON from HTML response: {}", parsedResponse);
                            return parsedResponse;
                        }
                    } catch (Exception e) {
                        logger.error("Failed to extract JSON from HTML response: {}", e.getMessage());
                    }
                    // 如果无法提取JSON，尝试直接使用响应体作为内容
                    logger.info("Using HTML response as content");
                    Map<String, Object> response = new HashMap<>();
                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> output = new HashMap<>();
                    output.put("result", responseBody);
                    data.put("output", output);
                    response.put("data", data);
                    return response;
                }
            }
            
            // 尝试将响应解析为JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> parsedResponse = objectMapper.readValue(responseBody, Map.class);
                logger.info("Workflow response parsed as JSON: {}", parsedResponse);
                return parsedResponse;
            } catch (Exception e) {
                logger.error("Failed to parse workflow response as JSON: {}", e.getMessage());
                // 如果解析失败，返回一个包含错误信息的Map
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to parse workflow response as JSON");
                errorResponse.put("status", "error");
                return errorResponse;
            }
        } catch (Exception e) {
            logger.error("Error processing consultation with workflow: {}", e.getMessage(), e);
            // 返回错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("status", "error");
            return errorResponse;
        }
    }
}