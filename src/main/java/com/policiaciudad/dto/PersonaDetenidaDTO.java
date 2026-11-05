package com.policiaciudad.dto;

import java.time.LocalDate;
import com.policiaciudad.modelo.PersonaDetenida;

// Datos de salida de una persona detenida
public class PersonaDetenidaDTO {

    private Integer id;
    private String nombreCompleto;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String mail;
    private String telefono;
    private Integer numeroBanda; // Puede ser null si no tiene banda

    public PersonaDetenidaDTO(Integer id, String nombreCompleto,
            Integer numeroBanda) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.numeroBanda = numeroBanda;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Integer getNumeroBanda() {
        return numeroBanda;
    }

    public PersonaDetenidaDTO(PersonaDetenida persona) {
        this.id = persona.getId();
        this.nombreCompleto = persona.getNombreCompleto();
        this.numeroBanda = persona.getNumeroBanda();
        this.nombre = persona.getNombre();
        this.apellido = persona.getApellido();
        this.dni = persona.getDni();
        this.fechaNacimiento = persona.getFechaNacimiento();
        this.edad = persona.calcularEdad();
        this.mail = persona.getMail();
        this.telefono = persona.getTelefono();
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
