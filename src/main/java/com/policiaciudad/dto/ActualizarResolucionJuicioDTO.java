package com.policiaciudad.dto;

public class ActualizarResolucionJuicioDTO {

    // Indica si la persona fue condenada
    private Boolean condenado;

    // Años de cárcel de la resolución
    private Integer aniosCarcel;

    public ActualizarResolucionJuicioDTO() {
    }

    public Boolean getCondenado() {
        return condenado;
    }

    public void setCondenado(Boolean condenado) {
        this.condenado = condenado;
    }

    public Integer getAniosCarcel() {
        return aniosCarcel;
    }

    public void setAniosCarcel(Integer aniosCarcel) {
        this.aniosCarcel = aniosCarcel;
    }
}
