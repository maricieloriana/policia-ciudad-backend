package com.policiaciudad.dto;

public class ActualizarArmaContratacionDTO {

    // Indica si el servicio se presta con arma
    private Boolean conArma;

    public ActualizarArmaContratacionDTO() {
    }

    public Boolean getConArma() {
        return conArma;
    }

    public void setConArma(Boolean conArma) {
        this.conArma = conArma;
    }
}