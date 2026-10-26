package com.policiaciudad.dto;

import java.time.LocalDate;

// Datos de salida de un asalto
public class AsaltoDTO {

    private Integer id;
    private int codigoSucursal;
    private int idPersonaDetenida;
    private LocalDate fecha;

    public AsaltoDTO(Integer id, int codigoSucursal,
            int idPersonaDetenida, LocalDate fecha) {

        this.id = id;
        this.codigoSucursal = codigoSucursal;
        this.idPersonaDetenida = idPersonaDetenida;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public int getCodigoSucursal() {
        return codigoSucursal;
    }

    public int getIdPersonaDetenida() {
        return idPersonaDetenida;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
