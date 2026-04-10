package com.pethealth.system.service;

import java.util.Map;

public interface ConsultationWorkflowService {
    Map<String, Object> processConsultation(Long consultationId, String symptoms, String petBreed, int petAge, String petName);
}