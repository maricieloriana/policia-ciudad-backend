package com.policiaciudad.service;

import com.policiaciudad.dto.*;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.*;
import com.policiaciudad.repository.*;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonasRegistroTest {
    private final PersonaRepository personas = mock(PersonaRepository.class);
    private final VigilanteRepository vigilantes = mock(VigilanteRepository.class);
    private final JuezRepository jueces = mock(JuezRepository.class);
    private final PersonaDetenidaRepository detenidas = mock(PersonaDetenidaRepository.class);
    private final UsuarioRepository usuarios = mock(UsuarioRepository.class);
    private final JuicioRepository juicios = mock(JuicioRepository.class);
    private final BandaRepository bandas = mock(BandaRepository.class);
    private final AsaltoRepository asaltos = mock(AsaltoRepository.class);
    private final ContratacionRepository contratos = mock(ContratacionRepository.class);

    private RegistroVigilanteDTO datos() {
        RegistroVigilanteDTO d = new RegistroVigilanteDTO();
        d.setNombre("Ana"); d.setApellido("Pérez"); d.setDni("00123456");
        d.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        d.setMail("ana@example.com"); d.setTelefono("11111111");
        return d;
    }

    @Test void registroDevuelveTodosLosDatos() {
        when(vigilantes.saveAndFlush(any(Vigilante.class))).thenAnswer(i -> i.getArgument(0));
        var servicio = new VigilanteServiceImpl(usuarios, vigilantes, contratos, personas);
        var respuesta = servicio.registrar(datos());
        assertEquals("Ana Pérez", respuesta.getNombreCompleto());
        assertEquals("00123456", respuesta.getDni());
        assertEquals("ana@example.com", respuesta.getMail());
        assertNotNull(respuesta.getEdad());
    }

    @Test void dniDuplicadoEnCualquierPersonaImpideRegistrar() {
        when(personas.existsByDni("00123456")).thenReturn(true);
        var servicio = new VigilanteServiceImpl(usuarios, vigilantes, contratos, personas);
        assertThrows(RegistroDuplicadoException.class, () -> servicio.registrar(datos()));
        verify(vigilantes, never()).saveAndFlush(any());
    }

    @Test void edicionConservaDniPropioYRecalculaEdad() {
        Vigilante persona = new Vigilante("Ana", "Pérez", "00123456",
                LocalDate.of(2000, 1, 1), "ana@example.com", "11111111");
        when(vigilantes.findById(1)).thenReturn(Optional.of(persona));
        when(vigilantes.saveAndFlush(any(Vigilante.class))).thenAnswer(i -> i.getArgument(0));
        var servicio = new VigilanteServiceImpl(usuarios, vigilantes, contratos, personas);
        var respuesta = servicio.actualizarDatos(1, datos());
        verify(personas).existsByDniAndIdNot("00123456", 1);
        assertEquals(persona.calcularEdad(), respuesta.getEdad());
    }

    @Test void juezMantieneClaveInternaDuranteEdicion() {
        Juez juez = new Juez("Ana", "Pérez", "00123456", LocalDate.of(2000, 1, 1),
                "ana@example.com", "11111111", 5001, 3);
        when(jueces.findById(1)).thenReturn(Optional.of(juez));
        when(jueces.saveAndFlush(any(Juez.class))).thenAnswer(i -> i.getArgument(0));
        RegistroJuezDTO d = new RegistroJuezDTO();
        d.setNombre("Ana"); d.setApellido("Pérez"); d.setDni("00123456");
        d.setFechaNacimiento(LocalDate.of(2000, 1, 1)); d.setMail("ana@example.com");
        d.setTelefono("11111111"); d.setClaveInterna(9999); d.setAniosServicio(4);
        var servicio = new JuezServiceImpl(jueces, juicios, usuarios, personas);
        assertEquals(5001, servicio.actualizarDatos(1, d).getClaveInterna());
    }

    @Test void detenidaPuedeRegistrarseSinBanda() {
        when(detenidas.saveAndFlush(any(PersonaDetenida.class))).thenAnswer(i -> i.getArgument(0));
        RegistroPersonaDetenidaDTO d = new RegistroPersonaDetenidaDTO();
        d.setNombre("Ana"); d.setApellido("Pérez"); d.setDni("00123456");
        d.setFechaNacimiento(LocalDate.of(2000, 1, 1)); d.setMail("ana@example.com");
        d.setTelefono("11111111");
        var servicio = new PersonaDetenidaServiceImpl(detenidas, bandas, asaltos, juicios, usuarios, personas);
        var respuesta = servicio.registrar(d);
        assertEquals("00123456", respuesta.getDni());
        assertNull(respuesta.getNumeroBanda());
        verifyNoInteractions(bandas);
    }
}
