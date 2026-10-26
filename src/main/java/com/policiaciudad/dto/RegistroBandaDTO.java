package com.policiaciudad.dto;

public class RegistroBandaDTO {

    // Datos que recibe la API para registrar una banda
    private Integer numero;
    private Integer cantidadMiembros;

    // Permite convertir el JSON recibido en este objeto
    public RegistroBandaDTO() {
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCantidadMiembros() {
        return cantidadMiembros;
    }

    public void setCantidadMiembros(Integer cantidadMiembros) {
        this.cantidadMiembros = cantidadMiembros;
    }
}
