package com.policiaciudad.service;

import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.modelo.Persona;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    // Valida todos los campos antes de modificar la entidad.
    public void actualizarDatosPersonales(Persona persona,
            String nombre, String apellido, String dni,
            LocalDate fechaNacimiento, String mail, String telefono) {

        String nombreValidado = validarTexto(nombre, "nombre", 100);
        String apellidoValidado = validarTexto(apellido, "apellido", 100);
        String dniValidado = validarTexto(dni, "DNI", 8);
        String mailValidado = validarTexto(mail, "mail", 254);
        String telefonoValidado = validarTexto(telefono, "teléfono", 30);

        if (!dniValidado.matches("[0-9]{8}")) {
            throw new DatosInvalidosException(
                    "El DNI debe contener exactamente 8 dígitos.");
        }

        LocalDate fechaActual = LocalDate.now(
                ZoneId.of("America/Argentina/Buenos_Aires"));

        if (fechaNacimiento == null || fechaNacimiento.isAfter(fechaActual)) {
            throw new DatosInvalidosException(
                    "La fecha de nacimiento es obligatoria y no puede ser futura.");
        }

        if (!mailValidado.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new DatosInvalidosException("El formato del mail no es válido.");
        }

        persona.setNombre(nombreValidado);
        persona.setApellido(apellidoValidado);
        persona.setDni(dniValidado);
        persona.setFechaNacimiento(fechaNacimiento);
        persona.setMail(mailValidado);
        persona.setTelefono(telefonoValidado);
        persona.setNombreCompleto(nombreValidado + " " + apellidoValidado);
    }

    private String validarTexto(String valor, String campo, int longitudMaxima) {
        if (valor == null || valor.isBlank()) {
            throw new DatosInvalidosException(
                    "El campo " + campo + " es obligatorio.");
        }
        String texto = valor.trim();
        if (texto.length() > longitudMaxima) {
            throw new DatosInvalidosException(
                    "El campo " + campo + " admite hasta "
                    + longitudMaxima + " caracteres.");
        }
        return texto;
    }

    public Integer calcularEdad(LocalDate fechaNacimiento) {
        return calcularEdad(fechaNacimiento, LocalDate.now(
                ZoneId.of("America/Argentina/Buenos_Aires")));
    }

    Integer calcularEdad(LocalDate fechaNacimiento, LocalDate fechaReferencia) {
        if (fechaReferencia == null) {
            throw new DatosInvalidosException(
                    "La fecha de referencia es obligatoria.");
        }
        if (fechaNacimiento == null) {
            return null;
        }
        if (fechaReferencia.isBefore(fechaNacimiento)) {
            throw new DatosInvalidosException(
                    "La fecha de referencia precede al nacimiento.");
        }
        return Period.between(fechaNacimiento, fechaReferencia).getYears();
    }
}
