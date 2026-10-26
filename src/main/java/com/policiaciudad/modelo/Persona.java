package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity // Permite guardar objetos de esta clase mediante JPA
@Table(name = "personas") // Nombre de la tabla en la base de datos
@Inheritance(strategy = InheritanceType.JOINED) // Separa los datos comunes de los propios de cada subclase
public class Persona {

    @Id // Identifica la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera el ID al guardar
    private Integer id; // Integer permite que el identificador sea null mientras la persona todavía no fue guardada

    @Column(name = "nombre_completo", nullable = false) // Columna que no admite NULL
    private String nombreCompleto; // private protege los atributos; se consultan mediante los métodos get

    protected Persona() {
        // Constructor vacío que JPA necesita para reconstruir objetos desde la base de datos
    }

    public Persona(String nombreCompleto) {
        cambiarNombre(nombreCompleto); // El constructor recibe el nombre y utiliza cambiarNombre para validarlo
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void cambiarNombre(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) { // isBlank detecta texto vacío o formado únicamente por espacios
            throw new DatosInvalidosException( // Rechaza un nombre inválido
                    "El nombre completo es obligatorio."
            );
        }

        // this.nombreCompleto identifica el atributo del objeto
        this.nombreCompleto = nombreCompleto.trim(); // trim elimina espacios al principio y al final
    }
}