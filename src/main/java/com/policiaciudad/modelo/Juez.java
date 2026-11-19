package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity // Permite guardar jueces mediante JPA
@Table(name = "jueces") // Tabla con los datos propios del juez
public class Juez extends Persona {

    @Column(name = "clave_interna", nullable = false, unique = true)
    private int claveInterna; // Código propio del juez; es diferente del id heredado, que identifica a la persona

    @Column(name = "anios_servicio", nullable = false)
    private int aniosServicio;

    public Juez() {
        // Constructor necesario para JPA.
    }

    public Juez(String nombreCompleto, int claveInterna, int aniosServicio) {
        super(nombreCompleto);
        this.claveInterna = claveInterna;
        this.aniosServicio = aniosServicio;
    }

    public Juez(String nombre, String apellido, String dni,
            LocalDate fechaNacimiento, String mail, String telefono,
            int claveInterna, int aniosServicio) {
        super(nombre, apellido, dni, fechaNacimiento, mail, telefono);
        this.claveInterna = claveInterna;
        this.aniosServicio = aniosServicio;
    }

    public int getClaveInterna() {
        return claveInterna;
    }

    public int getAniosServicio() {
        return aniosServicio;
    }

    public void setAniosServicio(int aniosServicio) {
        this.aniosServicio = aniosServicio;
    }
}
