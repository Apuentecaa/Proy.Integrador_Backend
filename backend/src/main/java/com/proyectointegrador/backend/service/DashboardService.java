package com.proyectointegrador.backend.service;

import com.proyectointegrador.backend.dto.DashboardDTO;
import com.proyectointegrador.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private CitaRepository citaRepository;

    public DashboardDTO obtenerDatosDashboard() {

        Long totalMedicos = medicoRepository.count();

        Long citasHoy = citaRepository.countByFecha(
                LocalDate.now());

        Long confirmadas = citaRepository.countByEstado(
                "confirmada");

        Long pendientes = citaRepository.countByEstado(
                "pendiente");

        return new DashboardDTO(
                totalMedicos,
                citasHoy,
                confirmadas,
                pendientes);
    }
}