package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.exception.JwtAuthenticationException;
import com.projeto.lab.implementacao.model.Usuario;
import com.projeto.lab.implementacao.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario authenticate(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new JwtAuthenticationException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new JwtAuthenticationException("Usuário ou senha inválidos");
        }

        return usuario;
    }

    public Usuario save(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
}