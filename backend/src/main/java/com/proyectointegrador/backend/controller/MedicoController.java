package com.proyectointegrador.backend.controller;

import com.proyectointegrador.backend.Entity.Medico;
import com.proyectointegrador.backend.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/medicos")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public List<Medico> listar() {
        return medicoService.listar();
    }

    @PostMapping
    public Medico guardar(@RequestBody Medico medico) {
        return medicoService.guardar(medico);
    }

    @PutMapping("/{id}")
    public Medico actualizar(@PathVariable long id, @RequestBody Medico medico) {
        return medicoService.actualizar(id, medico);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        medicoService.eliminar(id);
    }
}
