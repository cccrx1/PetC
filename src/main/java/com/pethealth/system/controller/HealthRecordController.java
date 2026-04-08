package com.pethealth.system.controller;

import com.pethealth.system.entity.HealthRecord;
import com.pethealth.system.entity.User;
import com.pethealth.system.service.HealthRecordService;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/health-records")
public class HealthRecordController {
    
    @Autowired
    private HealthRecordService healthRecordService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private Long getUserIdFromToken(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getProfile(username);
        return user.getId();
    }
    
    @PostMapping
    public ResponseEntity<HealthRecord> createHealthRecord(@RequestHeader("Authorization") String token, 
                                                         @RequestParam Long petId, 
                                                         @RequestBody HealthRecord healthRecord) {
        Long userId = getUserIdFromToken(token);
        HealthRecord createdHealthRecord = healthRecordService.createHealthRecord(healthRecord, petId, userId);
        return ResponseEntity.ok(createdHealthRecord);
    }
    
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByPetId(@RequestHeader("Authorization") String token, 
                                                                    @PathVariable Long petId) {
        Long userId = getUserIdFromToken(token);
        List<HealthRecord> healthRecords = healthRecordService.getHealthRecordsByPetId(petId, userId);
        return ResponseEntity.ok(healthRecords);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HealthRecord> getHealthRecordById(@RequestHeader("Authorization") String token, 
                                                          @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        HealthRecord healthRecord = healthRecordService.getHealthRecordById(id, userId);
        return ResponseEntity.ok(healthRecord);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HealthRecord> updateHealthRecord(@RequestHeader("Authorization") String token, 
                                                         @PathVariable Long id, 
                                                         @RequestBody HealthRecord healthRecord) {
        Long userId = getUserIdFromToken(token);
        HealthRecord updatedHealthRecord = healthRecordService.updateHealthRecord(id, healthRecord, userId);
        return ResponseEntity.ok(updatedHealthRecord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthRecord(@RequestHeader("Authorization") String token, 
                                                 @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        healthRecordService.deleteHealthRecord(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pet/{petId}/date-range")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByPetIdAndDateRange(
            @RequestHeader("Authorization") String token, 
            @PathVariable Long petId, 
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        Long userId = getUserIdFromToken(token);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<HealthRecord> healthRecords = healthRecordService.getHealthRecordsByPetIdAndDateRange(petId, start, end, userId);
        return ResponseEntity.ok(healthRecords);
    }
    
    @GetMapping("/pet/{petId}/type")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByPetIdAndType(
            @RequestHeader("Authorization") String token, 
            @PathVariable Long petId, 
            @RequestParam String type) {
        Long userId = getUserIdFromToken(token);
        List<HealthRecord> healthRecords = healthRecordService.getHealthRecordsByPetIdAndType(petId, type, userId);
        return ResponseEntity.ok(healthRecords);
    }
    
    @GetMapping("/pet/{petId}/type/date-range")
    public ResponseEntity<List<HealthRecord>> getHealthRecordsByPetIdTypeAndDateRange(
            @RequestHeader("Authorization") String token, 
            @PathVariable Long petId, 
            @RequestParam String type, 
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        Long userId = getUserIdFromToken(token);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<HealthRecord> healthRecords = healthRecordService.getHealthRecordsByPetIdTypeAndDateRange(petId, type, start, end, userId);
        return ResponseEntity.ok(healthRecords);
    }
}
