package com.proyectointegrador.backend.controller;

import com.proyectointegrador.backend.dto.CitaDTO;
import com.proyectointegrador.backend.Entity.Cita;
import com.proyectointegrador.backend.repository.CitaRepository;
import com.proyectointegrador.backend.service.CitaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/citas")
@CrossOrigin("*")
public class CitaController {

    @Autowired
    private CitaService service;

    @Autowired
    private CitaRepository repository;

    @GetMapping
    public List<CitaDTO> listar() {
        return service.listar();
    }

    @PutMapping("/{id}/estado")
    public Cita cambiarEstado(@PathVariable Long id, @RequestBody Cita cita) {

        Cita existente = repository.findById(id).orElseThrow();

        existente.setEstado(cita.getEstado());

        return repository.save(existente);
    }
}
