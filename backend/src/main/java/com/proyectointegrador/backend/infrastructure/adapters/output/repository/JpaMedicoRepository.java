package com.proyectointegrador.backend.infrastructure.adapters.output.persistence.repository;

import com.proyectointegrador.backend.infrastructure.adapters.output.persistence.entity.MedicoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMedicoRepository
                extends JpaRepository<MedicoEntity, Long> {
}