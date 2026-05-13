
package com.proyectointegrador.backend.application.services;

import com.proyectointegrador.backend.domain.model.User;
import com.proyectointegrador.backend.domain.ports.in.LoginUseCase;
import com.proyectointegrador.backend.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public AuthService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public boolean login(String username, String password) {
        Optional<User> user = userRepositoryPort.findByUsername(username);
        
        // Validación básica (luego implementaremos BCrypt para seguridad)
        return user.isPresent() && user.get().getPassword().equals(password);
    }
}