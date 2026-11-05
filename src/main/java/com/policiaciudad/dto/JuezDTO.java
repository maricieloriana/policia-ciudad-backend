package com.policiaciudad.dto;

import java.time.LocalDate;
//datos de salida de un juez
public class JuezDTO {

    private final Integer id;
    private final String nombreCompleto;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final LocalDate fechaNacimiento;
    private final Integer edad;
    private final String mail;
    private final String telefono;
    private final int claveInterna;
    private final int aniosServicio;

    public JuezDTO(
            Integer id,
            String nombreCompleto,
            String nombre,
            String apellido,
            String dni,
            LocalDate fechaNacimiento,
            Integer edad,
            String mail,
            String telefono,
            int claveInterna,
            int aniosServicio) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.mail = mail;
        this.telefono = telefono;
        this.claveInterna = claveInterna;
        this.aniosServicio = aniosServicio;
    }

    // Compatibilidad temporal con las llamadas anteriores del servicio.
    public JuezDTO(
            Integer id,
            String nombreCompleto,
            int claveInterna,
            int aniosServicio) {

        this(id, nombreCompleto, null, null, null,
                null, null, null, null,
                claveInterna, aniosServicio);
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

    public int getClaveInterna() {
        return claveInterna;
    }

    public int getAniosServicio() {
        return aniosServicio;
    }
}//RegistroJuezDTO recibe la solicitud; JuezDTO devuelve el resultado
