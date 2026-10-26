package com.policiaciudad.dto;

public class RegistroSucursalDTO {

    // Datos de una sucursal que recibirá la API
    private Integer codigo;
    private String domicilio;
    private Integer cantidadEmpleados;

    public RegistroSucursalDTO() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Integer getCantidadEmpleados() {
        return cantidadEmpleados;
    }

    public void setCantidadEmpleados(Integer cantidadEmpleados) {
        this.cantidadEmpleados = cantidadEmpleados;
    }
}