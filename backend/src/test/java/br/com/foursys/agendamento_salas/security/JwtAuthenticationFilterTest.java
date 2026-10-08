package br.com.foursys.agendamento_salas.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final JwtAuthenticationFilter filter =
            new JwtAuthenticationFilter(jwtService, userDetailsService);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveContinuarSemValidarQuandoAuthorizationNaoExiste() throws Exception {
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request(), new MockHttpServletResponse(), chain);

        assertEquals("/api/test", ((MockHttpServletRequest) chain.getRequest()).getRequestURI());
        verify(jwtService, never()).extractUsername(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveContinuarQuandoAuthorizationNaoUsaBearer() throws Exception {
        MockHttpServletRequest request = request();
        request.addHeader("Authorization", "Basic abc");
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertEquals(request, chain.getRequest());
        verify(jwtService, never()).extractUsername(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveAutenticarUsuarioQuandoTokenForValido() throws Exception {
        MockHttpServletRequest request = request();
        request.addHeader("Authorization", "Bearer token-valido");
        UserDetails user = User.withUsername("ana@example.com").password("hash").roles("USER").build();
        when(jwtService.extractUsername("token-valido")).thenReturn("ana@example.com");
        when(userDetailsService.loadUserByUsername("ana@example.com")).thenReturn(user);
        when(jwtService.isTokenValid("token-valido", user)).thenReturn(true);
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals("ROLE_USER", SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority());
        assertEquals(request, chain.getRequest());
    }

    @Test
    void naoDeveAutenticarQuandoTokenNaoForValido() throws Exception {
        MockHttpServletRequest request = request();
        request.addHeader("Authorization", "Bearer token-invalido");
        UserDetails user = User.withUsername("ana@example.com").password("hash").roles("USER").build();
        when(jwtService.extractUsername("token-invalido")).thenReturn("ana@example.com");
        when(userDetailsService.loadUserByUsername("ana@example.com")).thenReturn(user);
        when(jwtService.isTokenValid("token-invalido", user)).thenReturn(false);
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(request, chain.getRequest());
    }

    @Test
    void naoDeveCarregarUsuarioQuandoTokenNaoContemNome() throws Exception {
        MockHttpServletRequest request = request();
        request.addHeader("Authorization", "Bearer token-sem-nome");
        when(jwtService.extractUsername("token-sem-nome")).thenReturn(null);
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        verify(userDetailsService, never()).loadUserByUsername(org.mockito.ArgumentMatchers.any());
        assertEquals(request, chain.getRequest());
    }

    @Test
    void devePreservarAutenticacaoExistente() throws Exception {
        MockHttpServletRequest request = request();
        request.addHeader("Authorization", "Bearer novo-token");
        var existing = new UsernamePasswordAuthenticationToken("existente", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(existing);
        when(jwtService.extractUsername("novo-token")).thenReturn("ana@example.com");
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertEquals(existing, SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailsService, never()).loadUserByUsername(org.mockito.ArgumentMatchers.any());
        assertEquals(request, chain.getRequest());
    }

    private MockHttpServletRequest request() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/test");
        return request;
    }
}
