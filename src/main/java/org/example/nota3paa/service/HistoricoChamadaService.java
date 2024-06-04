package org.example.nota3paa.service;

import org.example.nota3paa.model.HistoricoChamada;
import org.example.nota3paa.repository.HistoricoChamadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricoChamadaService {
    @Autowired
    private HistoricoChamadaRepository historicoChamadaRepository;

    public void registrarChamada(Long pacienteId) {
        HistoricoChamada historicoChamada = new HistoricoChamada();
        historicoChamada.setPacienteId(pacienteId);
        historicoChamada.setHoraChamada(java.time.LocalDateTime.now());
        historicoChamadaRepository.save(historicoChamada);
    }
}
