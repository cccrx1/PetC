package com.pethealth.system.service;

import com.pethealth.system.entity.HealthRecord;
import java.time.LocalDateTime;
import java.util.List;

public interface HealthRecordService {
    HealthRecord createHealthRecord(HealthRecord healthRecord, Long petId, Long userId);
    List<HealthRecord> getHealthRecordsByPetId(Long petId, Long userId);
    HealthRecord getHealthRecordById(Long id, Long userId);
    HealthRecord updateHealthRecord(Long id, HealthRecord healthRecord, Long userId);
    void deleteHealthRecord(Long id, Long userId);
    List<HealthRecord> getHealthRecordsByPetIdAndDateRange(Long petId, LocalDateTime startDate, LocalDateTime endDate, Long userId);
    List<HealthRecord> getHealthRecordsByPetIdAndType(Long petId, String type, Long userId);
    List<HealthRecord> getHealthRecordsByPetIdTypeAndDateRange(Long petId, String type, LocalDateTime startDate, LocalDateTime endDate, Long userId);
}
