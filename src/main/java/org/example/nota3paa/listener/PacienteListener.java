package org.example.nota3paa.listener;

import org.example.nota3paa.model.Paciente;
import org.example.nota3paa.service.HistoricoChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
public class PacienteListener {
    @Autowired
    private HistoricoChamadaService historicoChamadaService;

    @RabbitListener(queues = "patientQueue")
    public void handlePatientCall(Paciente paciente) {
        historicoChamadaService.registrarChamada(paciente.getId());
    }
}
