package com.pethealth.system.service;

import com.pethealth.system.entity.Consultation;

import java.util.List;

public interface ConsultationService {
    Consultation createConsultation(Consultation consultation);
    Consultation getConsultationById(Long id);
    List<Consultation> getConsultationsByUserId(Long userId);
    List<Consultation> getConsultationsByPetId(Long petId);
    Consultation updateConsultation(Long id, Consultation consultation);
    Consultation archiveConsultation(Long id);
    void deleteConsultation(Long id);
}