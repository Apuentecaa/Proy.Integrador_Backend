package com.proyectointegrador.backend.domain.ports.input;

import com.proyectointegrador.backend.domain.model.Medico;
import java.util.List;

public interface MedicoServicePort {
    List<Medico> listarMedicos();

    Medico guardarMedico(Medico medico);

    Medico actualizarMedico(Long id, Medico medico);

    void eliminarMedico(Long id);
}
