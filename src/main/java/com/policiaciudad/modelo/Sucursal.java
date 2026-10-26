package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
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
        // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public Sucursal(int codigo, int codigoEntidadBancaria,
            String domicilio, int cantidadEmpleados) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de sucursal debe ser positivo."
            );
        }

        if (codigoEntidadBancaria <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de entidad bancaria debe ser positivo."
            );
        }

        if (domicilio == null || domicilio.isBlank()) {
            throw new DatosInvalidosException(
                    "El domicilio de la sucursal es obligatorio."
            );
        }

        this.codigo = codigo;
        this.codigoEntidadBancaria = codigoEntidadBancaria;
        this.domicilio = domicilio.trim();
        actualizarCantidadEmpleados(cantidadEmpleados);
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

    public void actualizarCantidadEmpleados(int cantidad) { // Permite modificar la cantidad y rechaza números negativos
        if (cantidad < 0) {
            throw new DatosInvalidosException(
                    "La cantidad de empleados no puede ser negativa."
            );
        }

        this.cantidadEmpleados = cantidad;
    }
    public void cambiarDomicilio(String domicilio) {

        // Rechaza domicilios nulos o formados solo por espacios
        if (domicilio == null || domicilio.isBlank()) {
            throw new DatosInvalidosException(
                    "El domicilio de la sucursal es obligatorio.");
        }

        // Respeta el tamaño de la columna en la base de datos
        if (domicilio.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El domicilio no debe superar 255 caracteres.");
        }

        this.domicilio = domicilio.trim();
    }
}