package com.policiaciudad.security;

import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

// Revalida las cuentas autenticadas antes de autorizar sus solicitudes.
// Se agrega solo a la cadena de Spring Security, no como filtro duplicado.
public class CuentaVigenteFilter extends OncePerRequestFilter {
    private final UsuarioRepository repositorio;

    public CuentaVigenteFilter(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String ruta = request.getServletPath();
        return ruta.startsWith("/css/") || ruta.startsWith("/js/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain cadena)
            throws ServletException, IOException {

        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion != null && autenticacion.isAuthenticated()
                && !(autenticacion instanceof AnonymousAuthenticationToken)) {
            Usuario cuenta = repositorio.findByNombreUsuario(autenticacion.getName())
                    .orElse(null);
            boolean vigente = cuenta != null
                    && cuenta.getNombreUsuario().equals(autenticacion.getName())
                    && autenticacion.getAuthorities().stream().anyMatch(autoridad ->
                            autoridad.getAuthority().equals("ROLE_" + cuenta.getRol().name()));
            if (!vigente) {
                new SecurityContextLogoutHandler().logout(request, response, autenticacion);
                if (request.getServletPath().startsWith("/api/")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"mensaje\":\"La cuenta cambio o fue eliminada. Inicia sesion nuevamente.\"}");
                } else {
                    response.sendRedirect(request.getContextPath() + "/login?cuentaActualizada");
                }
                return;
            }
        }
        cadena.doFilter(request, response);
    }
}
