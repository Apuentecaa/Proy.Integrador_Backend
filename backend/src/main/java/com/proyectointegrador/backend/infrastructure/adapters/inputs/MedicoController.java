package com.proyectointegrador.backend.infrastructure.adapters.input.rest;

import com.proyectointegrador.backend.domain.model.Medico;
import com.proyectointegrador.backend.domain.ports.input.MedicoServicePort;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/medicos")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicoController {

    private final MedicoServicePort servicePort;

    public MedicoController(MedicoServicePort servicePort) {
        this.servicePort = servicePort;
    }

    @GetMapping
    public List<Medico> listar() {
        return servicePort.listarMedicos();
    }

    @PostMapping
    public Medico guardar(@RequestBody Medico medico) {
        return servicePort.guardarMedico(medico);
    }

    @PutMapping("/{id}")
    public Medico actualizar(
            @PathVariable Long id,
            @RequestBody Medico medico) {

        return servicePort.actualizarMedico(id, medico);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicePort.eliminarMedico(id);
    }
}
