package com.proyectointegrador.backend.infrastructure.adapters.input;

import com.proyectointegrador.backend.domain.model.Usuario;
import com.proyectointegrador.backend.domain.ports.input.AuthServicePort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServicePort authServicePort;

    public AuthController(AuthServicePort authServicePort) {
        this.authServicePort = authServicePort;
    }

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {

        return authServicePort.registrar(usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {

        return authServicePort.login(
                usuario.getEmail(),
                usuario.getPassword());
    }
}