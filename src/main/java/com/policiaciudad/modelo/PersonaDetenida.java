package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity // Permite guardar personas detenidas mediante JPA
@Table(name = "personas_detenidas") // Tabla con sus datos propios
public class PersonaDetenida extends Persona {

    @Column(name = "numero_banda") // Admite NULL porque la banda es opcional
    private Integer numeroBanda; // Utiliza Integer porque puede ser null: significa que no tiene una banda asignada

    protected PersonaDetenida() {
        super(); // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public PersonaDetenida(String nombreCompleto) {
        super(nombreCompleto); // Reutiliza la validación del nombre de Persona
    }

    public Integer getNumeroBanda() {
        return numeroBanda;
    }

    public void asignarBanda(Integer numeroBanda) { // Recibe un número positivo
        if (numeroBanda == null || numeroBanda <= 0) {
            throw new DatosInvalidosException(
                    "El numero de banda debe ser positivo."
            );
        }

        this.numeroBanda = numeroBanda;
    }

    public void quitarBanda() { // Elimina la vinculación
        this.numeroBanda = null;
    }
    
    public PersonaDetenida(
            String nombre,
            String apellido,
            String dni,
            LocalDate fechaNacimiento,
            String mail,
            String telefono) {

        super();

        actualizarDatosPersonales(
                nombre,
                apellido,
                dni,
                fechaNacimiento,
                mail,
                telefono);
    }
}