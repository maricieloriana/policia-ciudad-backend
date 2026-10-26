package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity // Permite guardar asaltos mediante JPA
@Table(name = "asaltos") // Nombre de la tabla
public class Asalto {

    @Id // Clave primaria del registro
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera el ID
    private Integer id; // Identifica el registro

    @Column(name = "codigo_sucursal", nullable = false)
    private int codigoSucursal; // Vincula el registro con una sucursal

    @Column(name = "id_persona_detenida", nullable = false)
    private int idPersonaDetenida; // Vincula el registro con una persona detenida

    @Column(nullable = false)
    private LocalDate fecha; // Registra el día del hecho

    protected Asalto() {
        // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public Asalto(int codigoSucursal, int idPersonaDetenida,
            LocalDate fecha) {

        if (codigoSucursal <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de sucursal debe ser positivo."
            );
        }

        if (idPersonaDetenida <= 0) {
            throw new DatosInvalidosException(
                    "El ID de la persona detenida debe ser positivo."
            );
        }

        this.codigoSucursal = codigoSucursal;
        this.idPersonaDetenida = idPersonaDetenida;
        corregirFecha(fecha);
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

    public void corregirFecha(LocalDate fecha) { // Permite modificar la fecha y rechaza null
        if (fecha == null) {
            throw new DatosInvalidosException(
                    "La fecha del asalto es obligatoria."
            );
        }

        this.fecha = fecha;
    }
}