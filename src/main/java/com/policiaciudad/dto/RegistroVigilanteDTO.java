package com.policiaciudad.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

// Datos de entrada para registrar un vigilante
public class RegistroVigilanteDTO {

    private String nombre;
    private String apellido;
    private String dni;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)//Esto indica cómo interpretar la fecha enviada por el formulario
    private LocalDate fechaNacimiento; 
    private String mail;
    private String telefono;

    public RegistroVigilanteDTO() {
        // Permite cargar los datos recibidos en JSON
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