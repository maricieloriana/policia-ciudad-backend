package com.policiaciudad.service;

import com.policiaciudad.dto.RegistroVigilanteDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.modelo.Vigilante;
import com.policiaciudad.modelo.Vigilante;
import com.policiaciudad.repository.ContratacionRepository;
import com.policiaciudad.repository.PersonaRepository;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.repository.VigilanteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatosPersonalesTest {

    private final PersonaService personaService = new PersonaService();

    private final VigilanteRepository repositorio = mock(VigilanteRepository.class);
    private final VigilanteServiceImpl servicio = new VigilanteServiceImpl(
            mock(UsuarioRepository.class), repositorio,
            mock(ContratacionRepository.class), mock(PersonaRepository.class), personaService);

    private RegistroVigilanteDTO datos() {
        RegistroVigilanteDTO datos = new RegistroVigilanteDTO();
        datos.setNombre(" Ana ");
        datos.setApellido(" Pérez ");
        datos.setDni("00123456");
        datos.setFechaNacimiento(LocalDate.of(2000, 11, 6));
        datos.setMail("ana@example.com");
        datos.setTelefono("+54 11 1234-5678");
        return datos;
    }

    @Test
    void edadCambiaEnElCumpleaniosSinGuardar() {
        LocalDate nacimiento = LocalDate.of(2000, 11, 6);
        assertEquals(25, personaService.calcularEdad(nacimiento, LocalDate.of(2026, 11, 5)));
        assertEquals(26, personaService.calcularEdad(nacimiento, LocalDate.of(2026, 11, 6)));
        assertEquals(26, personaService.calcularEdad(nacimiento, LocalDate.of(2026, 11, 7)));
    }

    @Test
    void dniConservaCerosYRechazaFormatosIncorrectosAntesDeGuardar() {
        when(repositorio.saveAndFlush(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
        assertEquals("00123456", servicio.registrar(datos()).getDni());
        clearInvocations(repositorio);
        for (String dni : new String[]{"1234567", "123456789", "1234567A", "12.345.678"}) {
            RegistroVigilanteDTO solicitud = datos();
            solicitud.setDni(dni);
            assertThrows(DatosInvalidosException.class, () -> servicio.registrar(solicitud));
        }
        verify(repositorio, never()).saveAndFlush(any());
    }

    @Test
    void fechaFuturaYMailInvalidoSeRechazanEnElServicio() {
        RegistroVigilanteDTO solicitud = datos();
        solicitud.setFechaNacimiento(LocalDate.now().plusYears(1));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(solicitud));
        solicitud.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        solicitud.setMail("sin-arroba");
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(solicitud));
        verify(repositorio, never()).saveAndFlush(any());
    }

    @Test
    void cambioInvalidoNoModificaParcialmenteLaPersona() {
        Vigilante persona = new Vigilante("Ana", "Pérez", "00123456",
                LocalDate.of(2000, 1, 1), "ana@example.com", "11111111");
        persona.setNombreCompleto("Ana Pérez");
        when(repositorio.findById(1)).thenReturn(Optional.of(persona));
        RegistroVigilanteDTO solicitud = datos();
        solicitud.setNombre("Otro");
        solicitud.setMail("invalido");
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarDatos(1, solicitud));
        assertEquals("Ana", persona.getNombre());
        assertEquals("Ana Pérez", persona.getNombreCompleto());
        verify(repositorio, never()).saveAndFlush(any());
    }

    @Test
    void datosHistoricosSinFechaNoInventanEdad() {
        Vigilante persona = new Vigilante();
        persona.setNombreCompleto("Nombre histórico");
        when(repositorio.findAll()).thenReturn(List.of(persona));
        assertNull(servicio.listar().getFirst().getEdad());
        assertEquals("Nombre histórico", servicio.listar().getFirst().getNombreCompleto());
    }

    @Test
    void nacimientoBisiestoSeCalculaPorFechasCompletas() {
        LocalDate nacimiento = LocalDate.of(2000, 2, 29);
        assertEquals(24, personaService.calcularEdad(nacimiento, LocalDate.of(2025, 2, 28)));
        assertEquals(25, personaService.calcularEdad(nacimiento, LocalDate.of(2025, 3, 1)));
    }

    @Test
    void referenciaInvalidaSeRechazaYCamposObligatoriosSeValidan() {
        LocalDate nacimiento = LocalDate.of(2000, 1, 1);
        assertThrows(DatosInvalidosException.class, () -> personaService.calcularEdad(nacimiento, null));
        assertThrows(DatosInvalidosException.class,
                () -> personaService.calcularEdad(nacimiento, nacimiento.minusDays(1)));
        RegistroVigilanteDTO solicitud = datos();
        solicitud.setTelefono(" ");
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(solicitud));
        verify(repositorio, never()).saveAndFlush(any());
    }
}
