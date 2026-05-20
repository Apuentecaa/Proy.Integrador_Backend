package com.proyectointegrador.backend.application.service;

import com.proyectointegrador.backend.domain.model.Medico;
import com.proyectointegrador.backend.domain.ports.input.MedicoServicePort;
import com.proyectointegrador.backend.domain.ports.output.MedicoRepositoryPort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService implements MedicoServicePort {

    private final MedicoRepositoryPort repositoryPort;

    public MedicoService(MedicoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<Medico> listarMedicos() {
        return repositoryPort.obtenerTodos();
    }

    @Override
    public Medico guardarMedico(Medico medico) {
        return repositoryPort.guardar(medico);
    }

    @Override
    public Medico actualizarMedico(Long id, Medico medico) {
        return repositoryPort.actualizar(id, medico);
    }

    @Override
    public void eliminarMedico(Long id) {
        repositoryPort.eliminar(id);
    }
}
