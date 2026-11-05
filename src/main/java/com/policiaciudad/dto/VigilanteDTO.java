package com.policiaciudad.dto;

import java.time.LocalDate;

public class VigilanteDTO {

    private final Integer id;
    private final String nombreCompleto;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final LocalDate fechaNacimiento;
    private final Integer edad;
    private final String mail;
    private final String telefono;

    public VigilanteDTO(
            Integer id,
            String nombreCompleto,
            String nombre,
            String apellido,
            String dni,
            LocalDate fechaNacimiento,
            Integer edad,
            String mail,
            String telefono) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.mail = mail;
        this.telefono = telefono;
    }

    // Compatibilidad temporal con las llamadas anteriores del servicio.
    public VigilanteDTO(
            Integer id, String nombreCompleto, int edad) {

        this(id, nombreCompleto, null, null, null,
                null, edad, null, null);
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getMail() {
        return mail;
    }

    public String getTelefono() {
        return telefono;
    }
}