package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
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
        // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public Banda(int numero, int cantidadMiembros) {
        if (numero <= 0) {
            throw new DatosInvalidosException(
                    "El numero de banda debe ser positivo."
            );
        }

        this.numero = numero;
        actualizarCantidadMiembros(cantidadMiembros);
    }

    public int getNumero() {
        return numero;
    }

    public int getCantidadMiembros() {
        return cantidadMiembros;
    }

    public void actualizarCantidadMiembros(int cantidad) { // Permite modificar el total y rechaza cantidades negativas
        if (cantidad < 0) {
            throw new DatosInvalidosException(
                    "La cantidad de miembros no puede ser negativa."
            );
        }

        this.cantidadMiembros = cantidad;
    }
}