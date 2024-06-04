package org.example.nota3paa.repository;

import org.example.nota3paa.model.Paciente;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query("SELECT p FROM Paciente p WHERE p.status IS NULL")
    List<Paciente> findAllNaoChamados(Sort sort);

    @Query("SELECT p FROM Paciente p WHERE p.status = 'Chamado'")
    List<Paciente> findAllChamados(Sort sort);
}
