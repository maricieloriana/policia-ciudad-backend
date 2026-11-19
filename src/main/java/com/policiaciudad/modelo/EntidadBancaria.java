package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Permite guardar entidades bancarias mediante JPA
@Table(name = "entidades_bancarias") // Nombre de la tabla
public class EntidadBancaria {

    @Id // El código de la entidad es la clave primaria
    private int codigo; // Identifica la entidad y debe ser positivo

    @Column(name = "domicilio_central", nullable = false)
    private String domicilioCentral; // Guarda la dirección de su sede central

    protected EntidadBancaria() {
        // Constructor necesario para JPA.
    }

    public EntidadBancaria(int codigo, String domicilioCentral) {
        this.codigo = codigo;
        this.domicilioCentral = domicilioCentral;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDomicilioCentral() {
        return domicilioCentral;
    }

    public void setDomicilioCentral(String domicilioCentral) {
        this.domicilioCentral = domicilioCentral;
    }
}
