package org.example.nota3paa.service;

import org.example.nota3paa.model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendPatientCalledNotification(Paciente paciente) {
        messagingTemplate.convertAndSend("/topic/patientCalled", paciente);
    }
}
