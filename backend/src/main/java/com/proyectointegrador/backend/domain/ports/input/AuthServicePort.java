package com.proyectointegrador.backend.domain.ports.input;

import com.proyectointegrador.backend.domain.model.Usuario;

public interface AuthServicePort {

    Usuario registrar(Usuario usuario);

    Usuario login(String email, String password);
}