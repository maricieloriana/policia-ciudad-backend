package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity // Permite guardar personas detenidas mediante JPA
@Table(name = "personas_detenidas") // Tabla con sus datos propios
public class PersonaDetenida extends Persona {

    @Column(name = "numero_banda") // Admite NULL porque la banda es opcional
    private Integer numeroBanda; // Utiliza Integer porque puede ser null: significa que no tiene una banda asignada

    public PersonaDetenida() {
        // Constructor necesario para JPA.
    }

    public PersonaDetenida(String nombreCompleto) {
        super(nombreCompleto);
    }

    public PersonaDetenida(String nombre, String apellido, String dni,
            LocalDate fechaNacimiento, String mail, String telefono) {
        super(nombre, apellido, dni, fechaNacimiento, mail, telefono);
    }

    public Integer getNumeroBanda() {
        return numeroBanda;
    }

    public void setNumeroBanda(Integer numeroBanda) {
        this.numeroBanda = numeroBanda;
    }
}
