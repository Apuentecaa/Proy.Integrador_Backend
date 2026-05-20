package com.policlinico.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidad")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;
}
