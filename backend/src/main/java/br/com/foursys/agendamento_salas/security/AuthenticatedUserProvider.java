package br.com.foursys.agendamento_salas.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {
    public AuthenticatedUser get() {
        var authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Usuário autenticado não encontrado"
            );
        }

        return new AuthenticatedUser(principal.getId(), principal.getUsername());
    }
}
