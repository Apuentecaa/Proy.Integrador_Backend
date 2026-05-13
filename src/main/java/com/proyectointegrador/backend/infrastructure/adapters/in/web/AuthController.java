package com.proyectointegrador.backend.infrastructure.adapters.in.web;

import com.proyectointegrador.backend.domain.ports.in.LoginUseCase;
import com.proyectointegrador.backend.infrastructure.adapters.in.web.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Ajustado al puerto por defecto de Vite/React
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isAuthenticated = loginUseCase.login(request.getUsername(), request.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok(Map.of("message", "Login exitoso", "status", "success"));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas", "status", "error"));
        }
    }
}