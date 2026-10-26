package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity // Permite guardar vigilantes mediante JPA
@Table(name = "vigilantes") // Tabla con los datos propios del vigilante
public class Vigilante extends Persona { // Hereda los datos y métodos comunes definidos en Persona, sin repetirlos

    @Column(nullable = false) // La columna edad no admite NULL
    private int edad;

    protected Vigilante() {
        super(); // Llama al constructor vacío de Persona para que JPA reconstruya el objeto
    }

    public Vigilante(String nombreCompleto, int edad) {
        super(nombreCompleto); // Llama al constructor de Persona, que valida y guarda el nombre
        actualizarEdad(edad); // Valida los datos propios del vigilante
    }

    public int getEdad() {
        return edad;
    }

    public void actualizarEdad(int edad) { // Valida la edad antes de guardarla; el constructor utiliza esta misma validación
        if (edad < 0) {
            throw new DatosInvalidosException(
                    "La edad no puede ser negativa."
            );
        }

        this.edad = edad;
    }
}