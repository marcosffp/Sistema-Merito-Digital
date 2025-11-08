package com.projeto.lab.implementacao.config.filter;

import com.projeto.lab.implementacao.dto.JwtPayload;
import com.projeto.lab.implementacao.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/auth/") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs/") ||
            path.startsWith("/alunos/cadastro") || path.startsWith("/empresas/cadastro")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.substring(7);
        try {
            JwtPayload payload = jwtService.validateToken(token);

            JwtAuthenticationToken authentication = new JwtAuthenticationToken(payload);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            System.err.println("Token expirado: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token expirado");
            response.getWriter().flush();
        } catch (Exception e) {
            System.err.println("Erro ao validar token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
            response.getWriter().flush();
        }
    }
}