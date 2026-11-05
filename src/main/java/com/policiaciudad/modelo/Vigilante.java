package com.policiaciudad.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "vigilantes")
public class Vigilante extends Persona {

    protected Vigilante() {
        super();
    }

    public Vigilante(String nombre, String apellido, String dni,
            LocalDate fechaNacimiento, String mail, String telefono) {
        super();
        actualizarDatosPersonales(nombre, apellido, dni,
                fechaNacimiento, mail, telefono);
    }
}
