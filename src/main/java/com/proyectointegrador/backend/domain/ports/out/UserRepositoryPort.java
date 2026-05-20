package com.proyectointegrador.backend.domain.ports.out;

import com.proyectointegrador.backend.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
}