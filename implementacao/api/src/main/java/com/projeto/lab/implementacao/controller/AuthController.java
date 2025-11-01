package com.projeto.lab.implementacao.controller;

import com.projeto.lab.implementacao.dto.AuthRequest;
import com.projeto.lab.implementacao.dto.AuthResponse;
import com.projeto.lab.implementacao.model.Usuario;
import com.projeto.lab.implementacao.service.JwtService;
import com.projeto.lab.implementacao.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        Usuario usuario = usuarioService.authenticate(authRequest.email(), authRequest.senha());
        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}