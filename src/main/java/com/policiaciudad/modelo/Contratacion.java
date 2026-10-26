package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity // Permite guardar contrataciones mediante JPA
@Table(
        name = "contrataciones",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_contratacion_sucursal_vigilante_fecha",
                columnNames = {"codigo_sucursal", "id_vigilante", "fecha"}
        )
) // Declara que la combinación sucursal, vigilante y fecha no debe repetirse
public class Contratacion {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera el ID
    private Integer id;

    @Column(name = "codigo_sucursal", nullable = false)
    private int codigoSucursal; // Identifica la sucursal participante

    @Column(name = "id_vigilante", nullable = false)
    private int idVigilante; // Identifica al vigilante participante

    @Column(nullable = false)
    private LocalDate fecha; // Representa una fecha sin hora

    @Column(name = "con_arma", nullable = false)
    private boolean conArma; // Indica si el servicio es armado

    protected Contratacion() {
        // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public Contratacion(int codigoSucursal, int idVigilante,
            LocalDate fecha, boolean conArma) {

        if (codigoSucursal <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de sucursal debe ser positivo."
            );
        }

        if (idVigilante <= 0) {
            throw new DatosInvalidosException(
                    "El ID del vigilante debe ser positivo."
            );
        }

        this.codigoSucursal = codigoSucursal;
        this.idVigilante = idVigilante;
        cambiarFecha(fecha);
        cambiarCondicionArma(conArma);
    }

    public Integer getId() {
        return id;
    }

    public int getCodigoSucursal() {
        return codigoSucursal;
    }

    public int getIdVigilante() {
        return idVigilante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean isConArma() { // Se utiliza is porque devuelve un boolean
        return conArma;
    }

    public void cambiarFecha(LocalDate fecha) { // Impide que sea null
        if (fecha == null) {
            throw new DatosInvalidosException(
                    "La fecha de contratacion es obligatoria."
            );
        }

        this.fecha = fecha;
    }

    public void cambiarCondicionArma(boolean conArma) {
        this.conArma = conArma;
    }
}