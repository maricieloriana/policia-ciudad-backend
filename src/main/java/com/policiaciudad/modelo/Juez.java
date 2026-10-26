package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity // Permite guardar jueces mediante JPA
@Table(name = "jueces") // Tabla con los datos propios del juez
public class Juez extends Persona {

    @Column(name = "clave_interna", nullable = false, unique = true)
    private int claveInterna; // Código propio del juez; es diferente del id heredado, que identifica a la persona

    @Column(name = "anios_servicio", nullable = false)
    private int aniosServicio;

    protected Juez() {
        super(); // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public Juez(String nombreCompleto, int claveInterna,
            int aniosServicio) {

        super(nombreCompleto); // Reutiliza la validación del nombre de Persona

        if (claveInterna <= 0) {
            throw new DatosInvalidosException(
                    "La clave interna debe ser positiva."
            );
        }

        this.claveInterna = claveInterna;
        actualizarAniosServicio(aniosServicio);
    }

    public int getClaveInterna() {
        return claveInterna;
    }

    public int getAniosServicio() {
        return aniosServicio;
    }

    public void actualizarAniosServicio(int anios) { // Permite modificar la antigüedad y rechaza valores negativos
        if (anios < 0) {
            throw new DatosInvalidosException(
                    "Los anios de servicio no pueden ser negativos."
            );
        }

        this.aniosServicio = anios;
    }
}