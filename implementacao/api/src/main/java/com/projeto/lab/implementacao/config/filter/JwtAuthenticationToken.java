package com.projeto.lab.implementacao.config.filter;

import com.projeto.lab.implementacao.dto.JwtPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final JwtPayload payload;

    public JwtAuthenticationToken(JwtPayload payload) {
        super(null);
        this.payload = payload;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return payload.email();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public JwtPayload getPayload() {
        return payload;
    }
}