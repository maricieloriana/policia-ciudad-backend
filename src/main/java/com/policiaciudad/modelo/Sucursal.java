package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Permite guardar sucursales mediante JPA
@Table(name = "sucursales") // Nombre de la tabla
public class Sucursal {

    @Id // El código de sucursal es la clave primaria
    private int codigo; // Identifica la sucursal

    @Column(name = "codigo_entidad_bancaria", nullable = false)
    private int codigoEntidadBancaria; // Guarda el código de la entidad a la que pertenece

    @Column(nullable = false)
    private String domicilio; // Contiene la dirección de esta sucursal

    @Column(name = "cantidad_empleados", nullable = false)
    private int cantidadEmpleados;

    protected Sucursal() {
        // Constructor necesario para JPA.
    }

    public Sucursal(int codigo, int codigoEntidadBancaria,
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

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public int getCantidadEmpleados() {
        return cantidadEmpleados;
    }

    public void setCantidadEmpleados(int cantidadEmpleados) {
        this.cantidadEmpleados = cantidadEmpleados;
    }
}
