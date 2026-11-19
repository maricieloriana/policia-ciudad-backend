package com.policiaciudad.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "vigilantes")
public class Vigilante extends Persona {

    public Vigilante() {
        // Constructor necesario para JPA.
    }

public Vigilante(String nombre, String apellido, String dni,
            LocalDate fechaNacimiento, String mail, String telefono) {
        super(nombre, apellido, dni, fechaNacimiento, mail, telefono);
    }


}

