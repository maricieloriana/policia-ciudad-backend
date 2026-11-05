package com.policiaciudad.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

// Datos de entrada para registrar un juez
public class RegistroJuezDTO {

    private Integer claveInterna;
    private Integer aniosServicio;
    private String nombre;
    private String apellido;
    private String dni;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    private String mail;
    private String telefono;

    public RegistroJuezDTO() {
        // Permite cargar los datos recibidos en JSON
    }





    public Integer getClaveInterna() {
        return claveInterna;
    }

    public void setClaveInterna(Integer claveInterna) {
        this.claveInterna = claveInterna;
    }

    public Integer getAniosServicio() {
        return aniosServicio;
    }

    public void setAniosServicio(Integer aniosServicio) {
        this.aniosServicio = aniosServicio;
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
}
