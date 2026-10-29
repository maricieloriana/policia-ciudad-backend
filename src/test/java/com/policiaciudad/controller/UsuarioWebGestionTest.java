package com.policiaciudad.controller;

import com.policiaciudad.config.ConfiguracionSeguridad;
import com.policiaciudad.dto.UsuarioDTO;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.service.UsuarioService;
import jakarta.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioWebGestionTest {
    private AnnotationConfigWebApplicationContext contexto;
    private MockMvc mvc;
    private UsuarioService servicio;
    private UsuarioRepository repositorio;

    @TestConfiguration
    @EnableWebMvc
    @EnableWebSecurity
    static class ConfiguracionPrueba {
        @Bean UsuarioRepository usuarioRepository() { return mock(UsuarioRepository.class); }
        @Bean UsuarioService usuarioService() { return mock(UsuarioService.class); }
        @Bean UsuarioWebController controlador(UsuarioService servicio) { return new UsuarioWebController(servicio); }
        @Bean AccesoController acceso() { return new AccesoController(); }
        @Bean ThymeleafViewResolver vistas() {
            var plantillas = new ClassLoaderTemplateResolver();
            plantillas.setPrefix("templates/");
            plantillas.setSuffix(".html");
            plantillas.setTemplateMode("HTML");
            plantillas.setCharacterEncoding("UTF-8");
            var motor = new SpringTemplateEngine();
            motor.setTemplateResolver(plantillas);
            var resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(motor);
            resolver.setCharacterEncoding("UTF-8");
            return resolver;
        }
    }

    @BeforeEach void preparar() {
        contexto = new AnnotationConfigWebApplicationContext();
        contexto.setServletContext(new MockServletContext());
        contexto.register(ConfiguracionPrueba.class, ConfiguracionSeguridad.class);
        contexto.refresh();
        servicio = contexto.getBean(UsuarioService.class);
        repositorio = contexto.getBean(UsuarioRepository.class);
        mvc = MockMvcBuilders.webAppContextSetup(contexto)
                .addFilters(contexto.getBean("springSecurityFilterChain", Filter.class)).build();
        when(repositorio.findByNombreUsuario("administrador"))
                .thenReturn(Optional.of(new Usuario(2, "administrador", "hash", Rol.ADMINISTRADOR)));
        when(repositorio.findByNombreUsuario("investigador"))
                .thenReturn(Optional.of(new Usuario(3, "investigador", "hash", Rol.INVESTIGADOR)));
        when(repositorio.findByNombreUsuario("vigilante"))
                .thenReturn(Optional.of(new Usuario(12, "vigilante", "hash", Rol.VIGILANTE)));
        when(servicio.listar()).thenReturn(List.of(
                new UsuarioDTO(1, 2, "administrador", Rol.ADMINISTRADOR),
                new UsuarioDTO(4, 12, "vigilante", Rol.VIGILANTE)));
        when(servicio.consultarPorId(4)).thenReturn(new UsuarioDTO(4, 12, "vigilante", Rol.VIGILANTE));
    }

    @AfterEach void cerrar() { contexto.close(); SecurityContextHolder.clearContext(); }

    private MockHttpSession sesion(String nombre, Rol rol) {
        var sesion = new MockHttpSession(contexto.getServletContext());
        SecurityContext seguridad = SecurityContextHolder.createEmptyContext();
        seguridad.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(
                nombre, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()))));
        sesion.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, seguridad);
        return sesion;
    }

    @Test void administradorVeListaConAccionesYSinHashes() throws Exception {
        mvc.perform(get("/usuarios").session(sesion("administrador", Rol.ADMINISTRADOR)))
                .andExpect(status().isOk()).andExpect(view().name("usuarios"))
                .andExpect(content().string(containsString("Tu cuenta actual")))
                .andExpect(content().string(containsString("/usuarios/4/editar")))
                .andExpect(content().string(not(containsString("/usuarios/1/eliminar"))))
                .andExpect(content().string(not(containsString("password_hash"))));
    }

    @Test void formularioDeEdicionSeRenderizaConTitularYRol() throws Exception {
        mvc.perform(get("/usuarios/4/editar").session(sesion("administrador", Rol.ADMINISTRADOR)))
                .andExpect(status().isOk()).andExpect(view().name("usuario-edicion"))
                .andExpect(content().string(containsString("value=\"vigilante\"")))
                .andExpect(content().string(containsString("/usuarios/4/editar")))
                .andExpect(content().string(not(containsString("type=\"password\""))));
    }

    @Test void investigadorYVigilanteNoPuedenConsultarNiAbrirEdicion() throws Exception {
        for (var rol : List.of(Rol.INVESTIGADOR, Rol.VIGILANTE)) {
            String nombre = rol == Rol.INVESTIGADOR ? "investigador" : "vigilante";
            mvc.perform(get("/usuarios").session(sesion(nombre, rol))).andExpect(status().isForbidden());
            mvc.perform(get("/usuarios/4/editar").session(sesion(nombre, rol))).andExpect(status().isForbidden());
        }
        verify(servicio, never()).listar();
    }

    @Test void anonimoDebeIngresar() throws Exception {
        mvc.perform(get("/usuarios").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test void actualizacionConCsrfUsaElAdministradorAutenticado() throws Exception {
        var sesion = sesion("administrador", Rol.ADMINISTRADOR);
        MvcResult pagina = mvc.perform(get("/usuarios/4/editar").session(sesion)).andReturn();
        CsrfToken token = (CsrfToken) pagina.getRequest().getAttribute("_csrf");
        mvc.perform(post("/usuarios/4/editar").session(sesion)
                .param(token.getParameterName(), token.getToken())
                .param("nombreUsuario", "nuevo").param("rol", "INVESTIGADOR")
                .param("nombreAdministrador", "otro"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/usuarios"));
        verify(servicio).actualizar(4, "nuevo", Rol.INVESTIGADOR, "administrador");
    }

    @Test void eliminacionSinCsrfEsRechazada() throws Exception {
        mvc.perform(post("/usuarios/4/eliminar").session(sesion("administrador", Rol.ADMINISTRADOR)))
                .andExpect(status().isForbidden());
        verify(servicio, never()).eliminar(anyInt(), anyString());
    }

    @Test void otrosRolesNoPuedenGuardarNiEliminarInclusoConCsrf() throws Exception {
        for (var rol : List.of(Rol.INVESTIGADOR, Rol.VIGILANTE)) {
            String nombre = rol == Rol.INVESTIGADOR ? "investigador" : "vigilante";
            var sesion = sesion(nombre, rol);
            var pagina = mvc.perform(get("/login").session(sesion)).andReturn();
            CsrfToken token = (CsrfToken) pagina.getRequest().getAttribute("_csrf");
            mvc.perform(post("/usuarios/4/editar").session(sesion)
                    .param(token.getParameterName(), token.getToken())
                    .param("nombreUsuario", "nuevo").param("rol", "ADMINISTRADOR"))
                    .andExpect(status().isForbidden());
            mvc.perform(post("/usuarios/4/eliminar").session(sesion)
                    .param(token.getParameterName(), token.getToken()))
                    .andExpect(status().isForbidden());
        }
        verify(servicio, never()).actualizar(anyInt(), anyString(), any(), anyString());
        verify(servicio, never()).eliminar(anyInt(), anyString());
    }

    @Test void eliminacionConCsrfRedirigeALaLista() throws Exception {
        var sesion = sesion("administrador", Rol.ADMINISTRADOR);
        var pagina = mvc.perform(get("/usuarios").session(sesion)).andReturn();
        CsrfToken token = (CsrfToken) pagina.getRequest().getAttribute("_csrf");
        mvc.perform(post("/usuarios/4/eliminar").session(sesion)
                .param(token.getParameterName(), token.getToken()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/usuarios"));
        verify(servicio).eliminar(4, "administrador");
    }

    @Test void rolInvalidoVuelveAlFormularioSinGuardar() throws Exception {
        var sesion = sesion("administrador", Rol.ADMINISTRADOR);
        var pagina = mvc.perform(get("/usuarios/4/editar").session(sesion)).andReturn();
        CsrfToken token = (CsrfToken) pagina.getRequest().getAttribute("_csrf");
        mvc.perform(post("/usuarios/4/editar").session(sesion)
                .param(token.getParameterName(), token.getToken())
                .param("nombreUsuario", "nuevo").param("rol", "OTRO"))
                .andExpect(status().isOk()).andExpect(view().name("usuario-edicion"));
        verify(servicio, never()).actualizar(anyInt(), anyString(), any(), anyString());
    }
}
