package org.example.nota3paa.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String prioridade;
    private String status;
    private String numero;
}
