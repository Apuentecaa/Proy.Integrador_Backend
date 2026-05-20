package com.proyectointegrador.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectointegrador.backend.Entity.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
}
