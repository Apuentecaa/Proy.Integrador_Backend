package com.policlinico.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medico")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @Column(length = 100)
    private String sede;
}