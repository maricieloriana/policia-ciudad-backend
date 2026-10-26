package com.policiaciudad.dto;

// Datos de salida de una persona detenida
public class PersonaDetenidaDTO {

    private Integer id;
    private String nombreCompleto;
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
}