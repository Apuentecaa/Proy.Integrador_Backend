package com.proyectointegrador.backend.application.services;

import com.proyectointegrador.backend.domain.model.User;
import com.proyectointegrador.backend.domain.ports.in.LoginUseCase;
import com.proyectointegrador.backend.domain.ports.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder; // Nuevo import
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder; // Inyectamos el encriptador

    public AuthService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean login(String username, String password) {
        Optional<User> user = userRepositoryPort.findByUsername(username);
        
        // Comparamos el texto plano con el hash de la DB
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}