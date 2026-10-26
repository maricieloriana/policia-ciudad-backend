package com.policiaciudad.dto;

public class SucursalDTO {

    private int codigo;
    private int codigoEntidadBancaria;
    private String domicilio;
    private int cantidadEmpleados;

    // Prepara los datos que devuelve la API
    public SucursalDTO(int codigo, int codigoEntidadBancaria,
            String domicilio, int cantidadEmpleados) {

        this.codigo = codigo;
        this.codigoEntidadBancaria = codigoEntidadBancaria;
        this.domicilio = domicilio;
        this.cantidadEmpleados = cantidadEmpleados;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getCodigoEntidadBancaria() {
        return codigoEntidadBancaria;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public int getCantidadEmpleados() {
        return cantidadEmpleados;
    }
}
