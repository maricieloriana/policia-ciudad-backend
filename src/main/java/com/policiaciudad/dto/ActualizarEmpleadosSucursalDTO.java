package com.policiaciudad.dto;

public class ActualizarEmpleadosSucursalDTO {

    // Nueva cantidad de empleados de la sucursal
    private Integer cantidadEmpleados;

    public ActualizarEmpleadosSucursalDTO() {
    }

    public Integer getCantidadEmpleados() {
        return cantidadEmpleados;
    }

    public void setCantidadEmpleados(Integer cantidadEmpleados) {
        this.cantidadEmpleados = cantidadEmpleados;
    }
}