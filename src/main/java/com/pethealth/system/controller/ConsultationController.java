package com.pethealth.system.controller;

import com.pethealth.system.entity.Consultation;
import com.pethealth.system.entity.Pet;
import com.pethealth.system.entity.User;
import com.pethealth.system.service.ConsultationService;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
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

    @Autowired
    private ConsultationService consultationService;
    
    @Autowired
    private UserService userService;
    
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
        if (request.containsKey("petId")) {
            Long petId = ((Number) request.get("petId")).longValue();
            Pet pet = new Pet();
            pet.setId(petId);
            consultation.setPet(pet);
        }
        
        // 设置其他字段
        if (request.containsKey("type")) {
            consultation.setType((String) request.get("type"));
        }
        
        if (request.containsKey("symptoms")) {
            consultation.setDescription((String) request.get("symptoms"));
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
        return new ResponseEntity<>(createdConsultation, HttpStatus.CREATED);
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