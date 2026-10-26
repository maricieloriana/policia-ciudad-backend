package com.policiaciudad.dto;

public class BandaDTO {

    private int numero;
    private int cantidadMiembros;

    // Prepara los datos que devuelve la API
    public BandaDTO(int numero, int cantidadMiembros) {
        this.numero = numero;
        this.cantidadMiembros = cantidadMiembros;
    }

    public int getNumero() {
        return numero;
    }

    public int getCantidadMiembros() {
        return cantidadMiembros;
    }
}