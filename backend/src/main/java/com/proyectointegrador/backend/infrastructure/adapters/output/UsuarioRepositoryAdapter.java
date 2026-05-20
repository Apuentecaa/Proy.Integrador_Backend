package com.proyectointegrador.backend.infrastructure.adapters.output;

import com.proyectointegrador.backend.domain.model.Usuario;
import com.proyectointegrador.backend.domain.ports.output.UsuarioRepositoryPort;
import com.proyectointegrador.backend.infrastructure.adapters.output.entity.UsuarioEntity;
import com.proyectointegrador.backend.infrastructure.adapters.output.repository.JpaUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final JpaUsuarioRepository repository;

    public UsuarioRepositoryAdapter(JpaUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        UsuarioEntity entity = new UsuarioEntity(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getPassword(),
                usuario.getRol());

        UsuarioEntity saved = repository.save(entity);

        return new Usuario(
                saved.getId(),
                saved.getNombre(),
                saved.getEmail(),
                saved.getTelefono(),
                saved.getPassword(),
                saved.getRol());
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {

        return repository.findByEmail(email)
                .map(entity -> new Usuario(
                        entity.getId(),
                        entity.getNombre(),
                        entity.getEmail(),
                        entity.getTelefono(),
                        entity.getPassword(),
                        entity.getRol()));
    }
}