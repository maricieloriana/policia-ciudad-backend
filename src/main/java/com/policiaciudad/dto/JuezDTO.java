package com.policiaciudad.dto;

// Datos de salida de un juez
public class JuezDTO {

    private Integer id;
    private String nombreCompleto;
    private int claveInterna;
    private int aniosServicio;

    public JuezDTO(Integer id, String nombreCompleto,
            int claveInterna, int aniosServicio) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.claveInterna = claveInterna;
        this.aniosServicio = aniosServicio;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getClaveInterna() {
        return claveInterna;
    }

    public int getAniosServicio() {
        return aniosServicio;
    }
}//RegistroJuezDTO recibe la solicitud; JuezDTO devuelve el resultado
