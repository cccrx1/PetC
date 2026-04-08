package com.pethealth.system.controller;

import com.pethealth.system.entity.Consultation;
import com.pethealth.system.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation) {
        Consultation createdConsultation = consultationService.createConsultation(consultation);
        return new ResponseEntity<>(createdConsultation, HttpStatus.CREATED);
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