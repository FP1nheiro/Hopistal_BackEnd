package org.example.nota3paa.controller;

import org.example.nota3paa.model.Paciente;
import org.example.nota3paa.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public Paciente registrarPaciente(@RequestBody Paciente paciente) {
        return pacienteService.registrarPaciente(paciente);
    }

    @PutMapping("/{id}/prioridade")
    public Paciente atualizarPrioridade(@PathVariable Long id, @RequestBody String prioridade) {
        pacienteService.atualizarPrioridadePaciente(id, prioridade);
        return pacienteService.obterPacientePorId(id);
    }

    @GetMapping
    public List<Paciente> obterFilaPacientes() {
        return pacienteService.obterFilaPacientes();
    }

    @GetMapping("/chamados")
    public List<Paciente> obterPacientesChamados() {
        return pacienteService.obterPacientesChamados();
    }

    @PostMapping("/{id}/chamar")
    public void chamarPaciente(@PathVariable Long id) {
        pacienteService.chamarPaciente(id);
    }
}
