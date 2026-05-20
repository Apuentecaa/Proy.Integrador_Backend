package com.proyectointegrador.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectointegrador.backend.Entity.Paciente;

public interface PacienteRepository
        extends JpaRepository<Paciente, Long> {
}
