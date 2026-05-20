package com.proyectointegrador.backend.domain.ports.output;

import com.proyectointegrador.backend.domain.model.Medico;

import java.util.List;

public interface MedicoRepositoryPort {

    List<Medico> obtenerTodos();

    Medico guardar(Medico medico);

    Medico actualizar(Long id, Medico medico);

    void eliminar(Long id);
}
