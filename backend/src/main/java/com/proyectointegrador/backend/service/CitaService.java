package com.proyectointegrador.backend.service;

import com.proyectointegrador.backend.dto.CitaDTO;
import com.proyectointegrador.backend.Entity.Cita;
import com.proyectointegrador.backend.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository repository;

    public List<CitaDTO> listar() {

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    private CitaDTO convertirDTO(Cita cita) {

        CitaDTO dto = new CitaDTO();

        dto.setId(cita.getId());

        dto.setPaciente(
                cita.getPaciente().getNombre());

        dto.setMedico(
                cita.getMedico().getNombre());

        dto.setEspecialidad(
                cita.getMedico()
                        .getEspecialidad()
                        .getNombre());

        dto.setFecha(cita.getFecha());

        dto.setHora(cita.getHora());

        dto.setEstado(cita.getEstado());

        dto.setSede(
                cita.getMedico().getSede());

        return dto;
    }
}
