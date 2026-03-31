package com.pethealth.system.repository;

import com.pethealth.system.entity.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByPetId(Long petId);
    List<HealthRecord> findByPetIdAndRecordDateBetween(Long petId, LocalDateTime startDate, LocalDateTime endDate);
    List<HealthRecord> findByPetIdAndType(Long petId, String type);
    List<HealthRecord> findByPetIdAndTypeAndRecordDateBetween(Long petId, String type, LocalDateTime startDate, LocalDateTime endDate);
}
