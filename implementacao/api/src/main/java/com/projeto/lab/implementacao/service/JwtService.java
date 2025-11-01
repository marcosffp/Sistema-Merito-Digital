package com.projeto.lab.implementacao.service;

import com.projeto.lab.implementacao.dto.JwtPayload;
import com.projeto.lab.implementacao.exception.JwtAuthenticationException;
import com.projeto.lab.implementacao.model.Usuario;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000;

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("role", usuario.getClass().getSimpleName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public JwtPayload validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = claims.get("id", Long.class);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            return new JwtPayload(id, email, role);
        } catch (JwtAuthenticationException e) {
            throw new JwtAuthenticationException("token inválido", e);
        }
    }
}