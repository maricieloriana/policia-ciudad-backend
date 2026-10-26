package com.policiaciudad.dto;

// Datos de entrada para registrar un juicio resuelto
public class RegistroJuicioDTO {

    private Integer idAsalto;
    private Integer idPersonaDetenida;
    private Integer idJuez;
    private Boolean condenado;
    private Integer aniosCarcel;

    public RegistroJuicioDTO() {
        // Permite cargar los datos recibidos en JSON
    }

    public Integer getIdAsalto() {
        return idAsalto;
    }

    public void setIdAsalto(Integer idAsalto) {
        this.idAsalto = idAsalto;
    }

    public Integer getIdPersonaDetenida() {
        return idPersonaDetenida;
    }

    public void setIdPersonaDetenida(Integer idPersonaDetenida) {
        this.idPersonaDetenida = idPersonaDetenida;
    }

    public Integer getIdJuez() {
        return idJuez;
    }

    public void setIdJuez(Integer idJuez) {
        this.idJuez = idJuez;
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