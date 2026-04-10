package com.pethealth.system.controller;

import com.pethealth.system.entity.Consultation;
import com.pethealth.system.entity.Pet;
import com.pethealth.system.entity.User;
import com.pethealth.system.service.ConsultationService;
import com.pethealth.system.service.ConsultationWorkflowService;
import com.pethealth.system.service.PetService;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);

    @Autowired
    private ConsultationService consultationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private ConsultationWorkflowService consultationWorkflowService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> request) {
        // 从token中提取用户名
        String jwt = token.substring(7); // 去掉Bearer前缀
        String username = jwtUtil.extractUsername(jwt);
        User user = userService.getProfile(username);
        
        // 创建Consultation对象
        Consultation consultation = new Consultation();
        consultation.setUser(user);
        
        // 设置宠物
        Long petId = null;
        if (request.containsKey("petId")) {
            petId = ((Number) request.get("petId")).longValue();
            Pet pet = new Pet();
            pet.setId(petId);
            consultation.setPet(pet);
        }
        
        // 设置其他字段
        if (request.containsKey("type")) {
            consultation.setType((String) request.get("type"));
        }
        
        String symptoms = null;
        if (request.containsKey("symptoms")) {
            symptoms = (String) request.get("symptoms");
            consultation.setDescription(symptoms);
        }
        
        // 设置title字段
        if (request.containsKey("type")) {
            String type = (String) request.get("type");
            if ("online".equals(type)) {
                consultation.setTitle("在线咨询");
            } else if ("offline".equals(type)) {
                consultation.setTitle("线下预约");
            }
        }
        
        if (request.containsKey("status")) {
            consultation.setStatus((String) request.get("status"));
        } else {
            consultation.setStatus("pending");
        }
        
        if (request.containsKey("hospital")) {
            consultation.setHospitalName((String) request.get("hospital"));
        }
        
        if (request.containsKey("appointmentTime")) {
            try {
                String appointmentTimeStr = (String) request.get("appointmentTime");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime appointmentTime = LocalDateTime.parse(appointmentTimeStr, formatter);
                consultation.setAppointmentTime(appointmentTime);
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        
        Consultation createdConsultation = consultationService.createConsultation(consultation);
        
        // 调用工作流处理在线咨询
        if ("online".equals(createdConsultation.getType()) && symptoms != null && petId != null) {
            try {
                // 获取宠物详细信息
                Pet pet = petService.getPetById(petId, user.getId());
                if (pet != null) {
                    logger.info("Calling workflow to process consultation: {}", createdConsultation.getId());
                    // 调用工作流服务
                    Map<String, Object> workflowResponse = consultationWorkflowService.processConsultation(
                            createdConsultation.getId(),
                            symptoms,
                            pet.getBreed(),
                            pet.getAge(),
                            pet.getName()
                    );
                    
                    // 解析工作流响应
                    if (workflowResponse != null) {
                        logger.info("Workflow response received: {}", workflowResponse);
                        // 检查工作流是否返回错误
                        if (workflowResponse.containsKey("error")) {
                            logger.error("Workflow returned error: {}", workflowResponse.get("error"));
                            // 可以选择在这里添加错误处理逻辑
                        } else {
                            // 提取工作流的解答
                            String responseContent = extractResponseFromWorkflow(workflowResponse);
                            if (responseContent != null) {
                                // 检查响应内容长度，如果太长就截断
                                if (responseContent.length() > 100) {
                                    responseContent = responseContent.substring(0, 100) + "...";
                                    logger.warn("Workflow response truncated due to length");
                                }
                                // 更新咨询记录，添加工作流的响应
                                createdConsultation.setResponse(responseContent);
                                createdConsultation.setStatus("completed");
                                createdConsultation = consultationService.updateConsultation(createdConsultation.getId(), createdConsultation);
                                logger.info("Consultation updated with workflow response: {}", createdConsultation.getId());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略工作流调用错误，不影响咨询创建
                logger.error("Error processing consultation with workflow: {}", e.getMessage(), e);
            }
        }
        
        // 返回更新后的咨询对象
        return new ResponseEntity<>(createdConsultation, HttpStatus.CREATED);
    }
    
    /**
     * 从工作流响应中提取解答内容
     */
    private String extractResponseFromWorkflow(Map<String, Object> workflowResponse) {
        try {
            if (workflowResponse.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) workflowResponse.get("data");
                // 尝试第一种结构：data -> output -> result
                if (data.containsKey("output")) {
                    Map<String, Object> output = (Map<String, Object>) data.get("output");
                    if (output.containsKey("result")) {
                        return (String) output.get("result");
                    }
                }
                // 尝试第二种结构：data -> outputs -> json -> data -> output -> result
                if (data.containsKey("outputs")) {
                    Map<String, Object> outputs = (Map<String, Object>) data.get("outputs");
                    if (outputs.containsKey("json")) {
                        Map<String, Object> json = (Map<String, Object>) outputs.get("json");
                        if (json.containsKey("data")) {
                            Map<String, Object> jsonData = (Map<String, Object>) json.get("data");
                            if (jsonData.containsKey("output")) {
                                Map<String, Object> output = (Map<String, Object>) jsonData.get("output");
                                if (output.containsKey("result")) {
                                    return (String) output.get("result");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error extracting response from workflow: {}", e.getMessage());
        }
        return null;
    }
    
    @GetMapping
    public ResponseEntity<List<Consultation>> getConsultations(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.ok(List.of());
        }
        // 从token中提取用户名
        String jwt = token.substring(7); // 去掉Bearer前缀
        String username = jwtUtil.extractUsername(jwt);
        User user = userService.getProfile(username);
        List<Consultation> consultations = consultationService.getConsultationsByUserId(user.getId());
        return new ResponseEntity<>(consultations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id) {
        Consultation consultation = consultationService.getConsultationById(id);
        if (consultation != null) {
            return new ResponseEntity<>(consultation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Consultation>> getConsultationsByUserId(@PathVariable Long userId) {
        List<Consultation> consultations = consultationService.getConsultationsByUserId(userId);
        return new ResponseEntity<>(consultations, HttpStatus.OK);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Consultation>> getConsultationsByPetId(@PathVariable Long petId) {
        List<Consultation> consultations = consultationService.getConsultationsByPetId(petId);
        return new ResponseEntity<>(consultations, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long id, @RequestBody Consultation consultation) {
        Consultation updatedConsultation = consultationService.updateConsultation(id, consultation);
        if (updatedConsultation != null) {
            return new ResponseEntity<>(updatedConsultation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Consultation> archiveConsultation(@PathVariable Long id) {
        Consultation archivedConsultation = consultationService.archiveConsultation(id);
        if (archivedConsultation != null) {
            return new ResponseEntity<>(archivedConsultation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}