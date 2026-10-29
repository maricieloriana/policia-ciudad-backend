package com.policiaciudad.service;

import com.policiaciudad.exception.*;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioGestionTest {
    private UsuarioRepository usuarios;
    private PersonaRepository personas;
    private VigilanteRepository vigilantes;
    private UsuarioServiceImpl servicio;
    private Usuario admin;
    private Usuario cuenta;

    private Usuario cuenta(int id, int persona, String nombre, Rol rol) {
        Usuario usuario = new Usuario(persona, nombre, "hash-de-prueba", rol);
        ReflectionTestUtils.setField(usuario, "id", id);
        return usuario;
    }

    @BeforeEach
    void preparar() {
        usuarios = mock(UsuarioRepository.class);
        personas = mock(PersonaRepository.class);
        vigilantes = mock(VigilanteRepository.class);
        servicio = new UsuarioServiceImpl(personas, usuarios, vigilantes, mock(PasswordEncoder.class));
        admin = cuenta(1, 2, "administrador", Rol.ADMINISTRADOR);
        cuenta = cuenta(4, 12, "cuentaPrueba", Rol.VIGILANTE);
        when(usuarios.findById(4)).thenReturn(Optional.of(cuenta));
        when(usuarios.findByNombreUsuario("administrador")).thenReturn(Optional.of(admin));
        when(usuarios.bloquearPorRol(Rol.ADMINISTRADOR)).thenReturn(List.of(admin));
        when(usuarios.save(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    @Test void listaDTOsinContraseniasYOrdenados() {
        when(usuarios.findAll()).thenReturn(List.of(cuenta, admin));
        var resultado = servicio.listar();
        assertEquals(List.of(1, 4), resultado.stream().map(u -> u.getId()).toList());
        assertEquals("cuentaPrueba", resultado.get(1).getNombreUsuario());
        assertFalse(java.util.Arrays.stream(resultado.get(0).getClass().getDeclaredFields())
                .anyMatch(f -> f.getName().toLowerCase().matches(".*(password|contrasenia|hash).*")));
    }

    @Test void editaNombreYRolConservandoTitularYHash() {
        var resultado = servicio.actualizar(4, "  investigadorNuevo  ", Rol.INVESTIGADOR, "administrador");
        assertEquals("investigadorNuevo", resultado.getNombreUsuario());
        assertEquals(Rol.INVESTIGADOR, resultado.getRol());
        assertEquals(12, resultado.getIdPersona());
        assertEquals("hash-de-prueba", cuenta.getPasswordHash());
        verify(usuarios).save(cuenta);
    }

    @Test void rechazaNombreDuplicadoSinModificarCuenta() {
        when(usuarios.existsByNombreUsuarioAndIdNot("ocupado", 4)).thenReturn(true);
        assertThrows(RegistroDuplicadoException.class,
                () -> servicio.actualizar(4, "ocupado", Rol.INVESTIGADOR, "administrador"));
        assertEquals("cuentaPrueba", cuenta.getNombreUsuario());
        verify(usuarios, never()).save(any());
    }

    @Test void rechazaRolVigilanteSiElTitularNoEsVigilante() {
        assertThrows(DatosInvalidosException.class,
                () -> servicio.actualizar(4, "nuevo", Rol.VIGILANTE, "administrador"));
        verify(usuarios, never()).save(any());
    }

    @Test void admiteRolVigilanteParaTitularVigilante() {
        when(vigilantes.existsById(12)).thenReturn(true);
        assertEquals(Rol.VIGILANTE,
                servicio.actualizar(4, "nuevo", Rol.VIGILANTE, "administrador").getRol());
    }

    @Test void rechazaNombreVacioLargoYRolAusente() {
        assertThrows(DatosInvalidosException.class,
                () -> servicio.actualizar(4, " ", Rol.INVESTIGADOR, "administrador"));
        assertThrows(DatosInvalidosException.class,
                () -> servicio.actualizar(4, "x".repeat(256), Rol.INVESTIGADOR, "administrador"));
        assertThrows(DatosInvalidosException.class,
                () -> servicio.actualizar(4, "nuevo", null, "administrador"));
        verify(usuarios, never()).save(any());
    }

    @Test void eliminaSoloCuentaSinModificarPersona() {
        servicio.eliminar(4, "administrador");
        verify(usuarios).delete(cuenta);
        verifyNoInteractions(personas);
    }

    @Test void protegeLaCuentaEnUsoInclusoSiHayOtroAdministrador() {
        when(usuarios.findById(1)).thenReturn(Optional.of(admin));
        when(usuarios.bloquearPorRol(Rol.ADMINISTRADOR)).thenReturn(
                List.of(admin, cuenta(5, 3, "otroAdmin", Rol.ADMINISTRADOR)));
        assertThrows(RecursoEnUsoException.class, () -> servicio.eliminar(1, "administrador"));
        assertThrows(RecursoEnUsoException.class,
                () -> servicio.actualizar(1, "otroNombre", Rol.ADMINISTRADOR, "administrador"));
        verify(usuarios, never()).delete(any());
    }

    @Test void protegeAlUltimoAdministrador() {
        when(usuarios.findById(1)).thenReturn(Optional.of(admin));
        assertTrue(assertThrows(RecursoEnUsoException.class,
                () -> servicio.eliminar(1, "administrador")).getMessage().contains("al menos una"));
        assertThrows(RecursoEnUsoException.class,
                () -> servicio.actualizar(1, "administrador", Rol.INVESTIGADOR, "administrador"));
        verify(usuarios, never()).delete(any());
    }

    @Test void rechazaActorNoAdministrador() {
        when(usuarios.findByNombreUsuario("investigador"))
                .thenReturn(Optional.of(cuenta(2, 3, "investigador", Rol.INVESTIGADOR)));
        assertThrows(AccessDeniedException.class, () -> servicio.eliminar(4, "investigador"));
    }

    @Test void rechazaIdentificadoresInvalidosOInexistentes() {
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarPorId(0));
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultarPorId(99));
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.eliminar(99, "administrador"));
    }
}
