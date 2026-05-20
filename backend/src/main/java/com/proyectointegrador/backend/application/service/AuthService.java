package com.proyectointegrador.backend.application.service;

import com.proyectointegrador.backend.domain.model.Usuario;
import com.proyectointegrador.backend.domain.ports.input.AuthServicePort;
import com.proyectointegrador.backend.domain.ports.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServicePort {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public AuthService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Usuario registrar(Usuario usuario) {

        usuario.setRol("PACIENTE");

        return usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public Usuario login(String email, String password) {

        Usuario usuario = usuarioRepositoryPort.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}