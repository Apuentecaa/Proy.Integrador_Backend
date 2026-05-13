package com.proyectointegrador.backend.domain.ports.in;

public interface LoginUseCase {
    boolean login(String username, String password);
}