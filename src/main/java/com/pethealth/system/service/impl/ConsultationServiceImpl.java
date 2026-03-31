package com.pethealth.system.service.impl;

import com.pethealth.system.entity.Consultation;
import com.pethealth.system.repository.ConsultationRepository;
import com.pethealth.system.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Override
    public Consultation createConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation getConsultationById(Long id) {
        Optional<Consultation> optionalConsultation = consultationRepository.findById(id);
        return optionalConsultation.orElse(null);
    }

    @Override
    public List<Consultation> getConsultationsByUserId(Long userId) {
        return consultationRepository.findByUserId(userId);
    }

    @Override
    public List<Consultation> getConsultationsByPetId(Long petId) {
        return consultationRepository.findByPetId(petId);
    }

    @Override
    public Consultation updateConsultation(Long id, Consultation consultation) {
        Optional<Consultation> optionalConsultation = consultationRepository.findById(id);
        if (optionalConsultation.isPresent()) {
            Consultation existingConsultation = optionalConsultation.get();
            existingConsultation.setTitle(consultation.getTitle());
            existingConsultation.setDescription(consultation.getDescription());
            existingConsultation.setStatus(consultation.getStatus());
            existingConsultation.setResponse(consultation.getResponse());
            return consultationRepository.save(existingConsultation);
        }
        return null;
    }

    @Override
    public Consultation archiveConsultation(Long id) {
        Optional<Consultation> optionalConsultation = consultationRepository.findById(id);
        if (optionalConsultation.isPresent()) {
            Consultation consultation = optionalConsultation.get();
            consultation.setArchived(true);
            return consultationRepository.save(consultation);
        }
        return null;
    }

    @Override
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }
}