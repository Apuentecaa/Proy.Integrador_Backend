package com.proyectointegrador.backend.infrastructure.adapters.output.repository;

import com.proyectointegrador.backend.infrastructure.adapters.output.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);
}