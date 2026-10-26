package com.policiaciudad.dto;

import java.time.LocalDate;

public class ActualizarFechaContratacionDTO {

    // Nueva fecha de la contratación
    private LocalDate fecha;

    public ActualizarFechaContratacionDTO() {
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}