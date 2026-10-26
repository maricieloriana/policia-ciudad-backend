package com.policiaciudad.dto;

public class ActualizarBandaPersonaDetenidaDTO {

    // Número de banda; null indica que se quitará la asignación
    private Integer numeroBanda;

    public ActualizarBandaPersonaDetenidaDTO() {
    }

    public Integer getNumeroBanda() {
        return numeroBanda;
    }

    public void setNumeroBanda(Integer numeroBanda) {
        this.numeroBanda = numeroBanda;
    }
}