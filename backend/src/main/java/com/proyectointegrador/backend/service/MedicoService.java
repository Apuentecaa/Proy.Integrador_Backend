package com.proyectointegrador.backend.service;

import com.proyectointegrador.backend.Entity.Medico;
import com.proyectointegrador.backend.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    public Medico guardar(Medico medico) {
        return medicoRepository.save(medico);
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id).orElse(null);
    }

    public Medico actualizar(Long id, Medico medico) {
        Medico existente = medicoRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(medico.getNombre());
            existente.setApellido(medico.getApellido());
            existente.setTelefono(medico.getTelefono());
            existente.setEspecialidad(medico.getEspecialidad());
            return medicoRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Long id) {
        medicoRepository.deleteById(id);
    }
}