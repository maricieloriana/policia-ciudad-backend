package com.policiaciudad.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class RegistroPersonaDetenidaDTO {

    // Compatibilidad temporal con los controladores anteriores.

    private String nombre;
    private String apellido;
    private String dni;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    private String mail;
    private String telefono;
    private Integer numeroBanda;

    public RegistroPersonaDetenidaDTO() {
    }





    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getNumeroBanda() {
        return numeroBanda;
    }

    public void setNumeroBanda(Integer numeroBanda) {
        this.numeroBanda = numeroBanda;
    }
}