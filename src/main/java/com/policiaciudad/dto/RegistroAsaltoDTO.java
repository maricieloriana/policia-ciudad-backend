package com.policiaciudad.dto;

import java.time.LocalDate;

// Datos de entrada para registrar un asalto
public class RegistroAsaltoDTO {

    private Integer codigoSucursal;
    private Integer idPersonaDetenida;
    private LocalDate fecha;

    public RegistroAsaltoDTO() {
        // Permite cargar los datos recibidos en JSON
    }

    public Integer getCodigoSucursal() {
        return codigoSucursal;
    }

    public void setCodigoSucursal(Integer codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public Integer getIdPersonaDetenida() {
        return idPersonaDetenida;
    }

    public void setIdPersonaDetenida(Integer idPersonaDetenida) {
        this.idPersonaDetenida = idPersonaDetenida;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}