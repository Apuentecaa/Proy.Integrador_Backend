package com.proyectointegrador.backend.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicos")
public class MedicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especialidad;
    private String sede;
    private Double rating;
    private Integer pacientes;
    private String estado;

    // getters y setters
}