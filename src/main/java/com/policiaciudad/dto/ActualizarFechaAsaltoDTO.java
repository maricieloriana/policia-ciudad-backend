package com.policiaciudad.dto;

import java.time.LocalDate;

public class ActualizarFechaAsaltoDTO {

    // Fecha corregida del asalto
    private LocalDate fecha;

    public ActualizarFechaAsaltoDTO() {
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}