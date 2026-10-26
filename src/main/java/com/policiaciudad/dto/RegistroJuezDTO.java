package com.policiaciudad.dto;

// Datos de entrada para registrar un juez
public class RegistroJuezDTO {

    private String nombreCompleto;
    private Integer claveInterna;
    private Integer aniosServicio;

    public RegistroJuezDTO() {
        // Permite cargar los datos recibidos en JSON
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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
}