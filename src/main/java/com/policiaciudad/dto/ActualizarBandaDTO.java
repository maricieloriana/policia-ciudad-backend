package com.policiaciudad.dto;

public class ActualizarBandaDTO {

    // Nueva cantidad total de miembros de la banda
    private Integer cantidadMiembros;

    public ActualizarBandaDTO() {
    }

    public Integer getCantidadMiembros() {
        return cantidadMiembros;
    }

    public void setCantidadMiembros(Integer cantidadMiembros) {
        this.cantidadMiembros = cantidadMiembros;
    }
}