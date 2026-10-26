package com.policiaciudad.dto;

// Datos de salida de un juicio registrado
public class JuicioDTO {

    private Integer id;
    private int idAsalto;
    private int idPersonaDetenida;
    private int idJuez;
    private boolean condenado;
    private int aniosCarcel;

    public JuicioDTO(Integer id, int idAsalto,
            int idPersonaDetenida, int idJuez,
            boolean condenado, int aniosCarcel) {

        this.id = id;
        this.idAsalto = idAsalto;
        this.idPersonaDetenida = idPersonaDetenida;
        this.idJuez = idJuez;
        this.condenado = condenado;
        this.aniosCarcel = aniosCarcel;
    }

    public Integer getId() {
        return id;
    }

    public int getIdAsalto() {
        return idAsalto;
    }

    public int getIdPersonaDetenida() {
        return idPersonaDetenida;
    }

    public int getIdJuez() {
        return idJuez;
    }

    public boolean isCondenado() {
        return condenado;
    }

    public int getAniosCarcel() {
        return aniosCarcel;
    }
}