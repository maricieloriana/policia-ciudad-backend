package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatosPersonalesTest {
    private Vigilante persona() {
        return new Vigilante("Ana", "Pérez", "00123456",
                LocalDate.of(2000, 11, 6), "ana@example.com", "+54 11 1234-5678");
    }

    @Test void edadCambiaEnElCumpleaniosSinGuardar() {
        Vigilante persona = persona();
        assertEquals(25, persona.calcularEdad(LocalDate.of(2026, 11, 5)));
        assertEquals(26, persona.calcularEdad(LocalDate.of(2026, 11, 6)));
        assertEquals(26, persona.calcularEdad(LocalDate.of(2026, 11, 7)));
    }

    @Test void juecesYDetenidosHeredanLosDatosYElCalculo() {
        Juez juez = new Juez("Ana", "Pérez", "00123456",
                LocalDate.of(2000, 11, 6), "ana@example.com", "11111111", 5001, 3);
        PersonaDetenida detenida = new PersonaDetenida("Ana", "Pérez", "00123457",
                LocalDate.of(2000, 11, 6), "ana@example.com", "11111111");
        assertEquals(26, juez.calcularEdad(LocalDate.of(2026, 11, 6)));
        assertEquals(26, detenida.calcularEdad(LocalDate.of(2026, 11, 6)));
        assertNull(detenida.getNumeroBanda());
    }

    @Test void dniConservaCerosYRechazaFormatosIncorrectos() {
        assertEquals("00123456", persona().getDni());
        for (String dni : new String[]{"1234567", "123456789", "1234567A", "12.345.678"}) {
            assertThrows(DatosInvalidosException.class, () -> new Vigilante(
                    "Ana", "Pérez", dni, LocalDate.of(2000, 1, 1), "ana@example.com", "11111111"));
        }
    }

    @Test void fechaFuturaYMailInvalidoSeRechazan() {
        assertThrows(DatosInvalidosException.class, () -> new Vigilante("Ana", "Pérez",
                "12345678", LocalDate.now().plusYears(1), "ana@example.com", "11111111"));
        assertThrows(DatosInvalidosException.class, () -> new Vigilante("Ana", "Pérez",
                "12345678", LocalDate.of(2000, 1, 1), "sin-arroba", "11111111"));
    }

    @Test void cambioInvalidoNoModificaParcialmenteLaPersona() {
        Vigilante persona = persona();
        assertThrows(DatosInvalidosException.class, () -> persona.actualizarDatosPersonales(
                "Otro", "Nombre", "12345678", LocalDate.of(2000, 1, 1), "invalido", "11111111"));
        assertEquals("Ana", persona.getNombre());
        assertEquals("Ana Pérez", persona.getNombreCompleto());
    }

    @Test void datosHistoricosSinFechaNoInventanEdad() {
        Persona persona = new Persona("Nombre histórico");
        assertNull(persona.calcularEdad());
        assertEquals("Nombre histórico", persona.getNombreCompleto());
    }

    @Test void nacimientoBisiestoSeCalculaPorFechasCompletas() {
        Vigilante persona = new Vigilante("Ana", "Pérez", "12345678",
                LocalDate.of(2000, 2, 29), "ana@example.com", "11111111");
        assertEquals(24, persona.calcularEdad(LocalDate.of(2025, 2, 28)));
        assertEquals(25, persona.calcularEdad(LocalDate.of(2025, 3, 1)));
    }
}
