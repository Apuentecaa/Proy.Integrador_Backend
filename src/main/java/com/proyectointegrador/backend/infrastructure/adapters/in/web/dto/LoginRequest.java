package com.proyectointegrador.backend.infrastructure.adapters.in.web.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}