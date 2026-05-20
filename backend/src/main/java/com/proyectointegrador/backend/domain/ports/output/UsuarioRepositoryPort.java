package com.proyectointegrador.backend.domain.ports.output;

import com.proyectointegrador.backend.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);
}