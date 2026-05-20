package com.proyectointegrador.backend.infrastructure.adapters.output.persistence.adapter;

import com.proyectointegrador.backend.domain.model.Medico;
import com.proyectointegrador.backend.domain.ports.output.MedicoRepositoryPort;
import com.proyectointegrador.backend.infrastructure.adapters.output.persistence.entity.MedicoEntity;
import com.proyectointegrador.backend.infrastructure.adapters.output.persistence.repository.JpaMedicoRepository;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MedicoPersistenceAdapter
        implements MedicoRepositoryPort {

    private final JpaMedicoRepository repository;

    public MedicoPersistenceAdapter(JpaMedicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Medico> obtenerTodos() {

        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Medico guardar(Medico medico) {

        MedicoEntity entity = toEntity(medico);

        return toDomain(repository.save(entity));
    }

    @Override
    public Medico actualizar(Long id, Medico medico) {

        MedicoEntity entity = toEntity(medico);

        entity.setId(id);

        return toDomain(repository.save(entity));
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private Medico toDomain(MedicoEntity entity) {

        return new Medico(
                entity.getId(),
                entity.getNombre(),
                entity.getEspecialidad(),
                entity.getSede(),
                entity.getRating(),
                entity.getPacientes(),
                entity.getEstado());
    }

    private MedicoEntity toEntity(Medico medico) {

        MedicoEntity entity = new MedicoEntity();

        entity.setId(medico.getId());
        entity.setNombre(medico.getNombre());
        entity.setEspecialidad(medico.getEspecialidad());
        entity.setSede(medico.getSede());
        entity.setRating(medico.getRating());
        entity.setPacientes(medico.getPacientes());
        entity.setEstado(medico.getEstado());

        return entity;
    }
}