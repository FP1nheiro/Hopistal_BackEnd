package org.example.nota3paa.service;

import org.example.nota3paa.model.HistoricoChamada;
import org.example.nota3paa.model.Paciente;
import org.example.nota3paa.repository.HistoricoChamadaRepository;
import org.example.nota3paa.repository.PacienteRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoricoChamadaRepository historicoChamadaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SmsService smsService;

    public Paciente registrarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void atualizarPrioridadePaciente(Long id, String prioridade) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        paciente.setPrioridade(prioridade);
        pacienteRepository.save(paciente);
    }

    public List<Paciente> obterFilaPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAllNaoChamados(Sort.by(Sort.Direction.DESC, "prioridade"));
        pacientes.sort(Comparator.comparingInt(this::getPrioridadeValor));
        return pacientes;
    }

    public List<Paciente> obterPacientesChamados() {
        List<Paciente> pacientes = pacienteRepository.findAllChamados(Sort.by(Sort.Direction.DESC, "prioridade"));
        pacientes.sort(Comparator.comparingInt(this::getPrioridadeValor));
        return pacientes;
    }

    public Paciente obterPacientePorId(Long id) {
        return pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public void chamarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        paciente.setStatus("Chamado");
        pacienteRepository.save(paciente);
        rabbitTemplate.convertAndSend("patientExchange", "patient.call", paciente);

        HistoricoChamada historicoChamada = new HistoricoChamada();
        historicoChamada.setPacienteId(paciente.getId());
        historicoChamada.setHoraChamada(LocalDateTime.now());
        historicoChamadaRepository.save(historicoChamada);

        // Enviar notificação via WebSocket
        messagingTemplate.convertAndSend("/topic/patientCalled", paciente);

        // Enviar SMS via Twilio
        String message = "Sua vez de ser atendido chegou!";
        smsService.sendSms(paciente.getNumero(), message);
    }

    private int getPrioridadeValor(Paciente paciente) {
        switch (paciente.getPrioridade()) {
            case "Emergência":
                return 1;
            case "Muito Urgente":
                return 2;
            case "Urgente":
                return 3;
            case "Pouco Urgente":
                return 4;
            case "Não Urgente":
                return 5;
            default:
                return 6;
        }
    }
}
