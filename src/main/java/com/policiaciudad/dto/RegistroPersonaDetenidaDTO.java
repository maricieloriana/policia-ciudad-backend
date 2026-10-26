package com.policiaciudad.dto;

// Datos de entrada para registrar una persona detenida
public class RegistroPersonaDetenidaDTO {

    private String nombreCompleto;
    private Integer numeroBanda; // Es opcional: null significa sin banda

    public RegistroPersonaDetenidaDTO() {
        // Permite cargar los datos recibidos en JSON
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getNumeroBanda() {
        return numeroBanda;
    }

    public void setNumeroBanda(Integer numeroBanda) {
        this.numeroBanda = numeroBanda;
    }
}