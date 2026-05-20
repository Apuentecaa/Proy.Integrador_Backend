package com.proyectointegrador.backend.repository;

import com.proyectointegrador.backend.Entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface CitaRepository
        extends JpaRepository<Cita, Long> {

    Long countByEstado(String estado);

    Long countByFecha(LocalDate fecha);
}