package com.policiaciudad.security;

import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaVigenteFilterTest {
    @AfterEach void limpiar() { SecurityContextHolder.clearContext(); }

    private void autenticar(String nombre, Rol rol) {
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(nombre, null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()))));
    }

    @Test void cuentaSinCambiosContinua() throws Exception {
        var repo = mock(UsuarioRepository.class);
        when(repo.findByNombreUsuario("usuario"))
                .thenReturn(Optional.of(new Usuario(12, "usuario", "hash", Rol.VIGILANTE)));
        autenticar("usuario", Rol.VIGILANTE);
        var request = new MockHttpServletRequest("GET", "/mi-perfil");
        request.setServletPath("/mi-perfil");
        var continua = new AtomicBoolean();
        new CuentaVigenteFilter(repo).doFilter(request, new MockHttpServletResponse(),
                (a, b) -> continua.set(true));
        assertTrue(continua.get());
    }

    @Test void cuentaEliminadaInvalidaSesionYRedirige() throws Exception {
        var repo = mock(UsuarioRepository.class);
        autenticar("eliminado", Rol.VIGILANTE);
        var request = new MockHttpServletRequest("GET", "/mi-perfil");
        request.setServletPath("/mi-perfil");
        var sesion = request.getSession();
        var response = new MockHttpServletResponse();
        var continua = new AtomicBoolean();
        new CuentaVigenteFilter(repo).doFilter(request, response, (a, b) -> continua.set(true));
        assertFalse(continua.get());
        assertEquals("/login?cuentaActualizada", response.getRedirectedUrl());
        assertThrows(IllegalStateException.class, () -> sesion.getAttribute("cualquiera"));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test void cambioDeRolRetiraPermisosAnteriores() throws Exception {
        var repo = mock(UsuarioRepository.class);
        when(repo.findByNombreUsuario("usuario"))
                .thenReturn(Optional.of(new Usuario(12, "usuario", "hash", Rol.VIGILANTE)));
        autenticar("usuario", Rol.ADMINISTRADOR);
        var request = new MockHttpServletRequest("GET", "/api/usuarios");
        request.setServletPath("/api/usuarios");
        var response = new MockHttpServletResponse();
        new CuentaVigenteFilter(repo).doFilter(request, response,
                (a, b) -> fail("No debe autorizar una cuenta con permisos antiguos"));
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("mensaje"));
    }

    @Test void noInterfiereConElAccesoAnonimo() throws Exception {
        var repo = mock(UsuarioRepository.class);
        var continua = new AtomicBoolean();
        new CuentaVigenteFilter(repo).doFilter(new MockHttpServletRequest("GET", "/login"),
                new MockHttpServletResponse(), (a, b) -> continua.set(true));
        assertTrue(continua.get());
        verifyNoInteractions(repo);
    }
}
