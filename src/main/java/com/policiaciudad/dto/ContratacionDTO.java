package com.policiaciudad.dto;

import java.time.LocalDate;

public class ContratacionDTO {

    private Integer id; //puede ser null al solicitar el registro; en la respuesta contendrá el identificador asignado
    private int codigoSucursal;
    private int idVigilante;
    private LocalDate fecha;
    private boolean conArma;
//El constructor vacío y los setters permiten cargar los datos recibidos.
    public ContratacionDTO() {
    }
//El constructor con parámetros permite construir la respuesta
    public ContratacionDTO(Integer id, int codigoSucursal,
            int idVigilante, LocalDate fecha, boolean conArma) {

        this.id = id;
        this.codigoSucursal = codigoSucursal;
        this.idVigilante = idVigilante;
        this.fecha = fecha;
        this.conArma = conArma;
    }
//Los getters permiten leer los datos y generar el JSON
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCodigoSucursal() {
        return codigoSucursal;
    }

    public void setCodigoSucursal(int codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public int getIdVigilante() {
        return idVigilante;
    }

    public void setIdVigilante(int idVigilante) {
        this.idVigilante = idVigilante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isConArma() {
        return conArma;
    }

    public void setConArma(boolean conArma) {
        this.conArma = conArma;
    }
}