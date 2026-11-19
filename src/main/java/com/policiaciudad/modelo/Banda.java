package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Permite guardar bandas mediante JPA
@Table(name = "bandas") // Nombre de la tabla en la base de datos
public class Banda {

    @Id // El número de banda es la clave primaria
    private int numero; // Identifica la banda y debe ser positivo

    @Column(name = "cantidad_miembros", nullable = false)
    private int cantidadMiembros; // Registra el total de integrantes; puede incluir personas que no están detenidas

    protected Banda() {
        // Constructor necesario para JPA.
    }

    public Banda(int numero, int cantidadMiembros) {
        this.numero = numero;
        this.cantidadMiembros = cantidadMiembros;
    }

    public int getNumero() {
        return numero;
    }

    public int getCantidadMiembros() {
        return cantidadMiembros;
    }

    public void setCantidadMiembros(int cantidadMiembros) {
        this.cantidadMiembros = cantidadMiembros;
    }
}
