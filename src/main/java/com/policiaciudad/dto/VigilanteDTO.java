package com.policiaciudad.dto;

// Datos personales que se devolverán en la consulta del vigilante
public class VigilanteDTO {

    private Integer id; //el identificador del vigilante heredado de Persona
    private String nombreCompleto;
    private int edad; //el dato propio de Vigilante.

    public VigilanteDTO(Integer id, String nombreCompleto, int edad) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }
}