package com.pethealth.system.service.impl;

import com.pethealth.system.entity.HealthRecord;
import com.pethealth.system.entity.Pet;
import com.pethealth.system.repository.HealthRecordRepository;
import com.pethealth.system.service.HealthRecordService;
import com.pethealth.system.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {
    
    @Autowired
    private HealthRecordRepository healthRecordRepository;
    
    @Autowired
    private PetService petService;
    
    @Override
    public HealthRecord createHealthRecord(HealthRecord healthRecord, Long petId, Long userId) {
        Pet pet = petService.getPetById(petId, userId);
        healthRecord.setPet(pet);
        return healthRecordRepository.save(healthRecord);
    }
    
    @Override
    public List<HealthRecord> getHealthRecordsByPetId(Long petId, Long userId) {
        petService.getPetById(petId, userId);
        return healthRecordRepository.findByPetId(petId);
    }
    
    @Override
    public HealthRecord getHealthRecordById(Long id, Long userId) {
        Optional<HealthRecord> healthRecordOptional = healthRecordRepository.findById(id);
        if (healthRecordOptional.isPresent()) {
            HealthRecord healthRecord = healthRecordOptional.get();
            if (healthRecord.getPet().getUser().getId().equals(userId)) {
                return healthRecord;
            }
            throw new RuntimeException("Health record not found for this user");
        }
        throw new RuntimeException("Health record not found");
    }
    
    @Override
    public HealthRecord updateHealthRecord(Long id, HealthRecord healthRecord, Long userId) {
        HealthRecord existingHealthRecord = getHealthRecordById(id, userId);
        existingHealthRecord.setRecordDate(healthRecord.getRecordDate());
        existingHealthRecord.setWeight(healthRecord.getWeight());
        existingHealthRecord.setDiet(healthRecord.getDiet());
        existingHealthRecord.setExercise(healthRecord.getExercise());
        existingHealthRecord.setNotes(healthRecord.getNotes());
        existingHealthRecord.setType(healthRecord.getType());
        return healthRecordRepository.save(existingHealthRecord);
    }
    
    @Override
    public void deleteHealthRecord(Long id, Long userId) {
        HealthRecord healthRecord = getHealthRecordById(id, userId);
        healthRecordRepository.delete(healthRecord);
    }
    
    @Override
    public List<HealthRecord> getHealthRecordsByPetIdAndDateRange(Long petId, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        petService.getPetById(petId, userId);
        return healthRecordRepository.findByPetIdAndRecordDateBetween(petId, startDate, endDate);
    }
    
    @Override
    public List<HealthRecord> getHealthRecordsByPetIdAndType(Long petId, String type, Long userId) {
        petService.getPetById(petId, userId);
        return healthRecordRepository.findByPetIdAndType(petId, type);
    }
    
    @Override
    public List<HealthRecord> getHealthRecordsByPetIdTypeAndDateRange(Long petId, String type, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        petService.getPetById(petId, userId);
        return healthRecordRepository.findByPetIdAndTypeAndRecordDateBetween(petId, type, startDate, endDate);
    }
}
