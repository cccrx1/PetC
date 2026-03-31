package com.pethealth.system.repository;

import com.pethealth.system.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByUserId(Long userId);
    List<Consultation> findByPetId(Long petId);
}