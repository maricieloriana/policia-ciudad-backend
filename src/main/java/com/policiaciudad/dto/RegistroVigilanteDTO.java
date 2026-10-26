package com.policiaciudad.dto;

// Datos de entrada para registrar un vigilante
public class RegistroVigilanteDTO {

    private String nombreCompleto;
    private Integer edad; // Permite detectar si no se informó la edad

    public RegistroVigilanteDTO() {
        // Permite cargar los datos recibidos en JSON
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}