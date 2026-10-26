package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Permite guardar entidades bancarias mediante JPA
@Table(name = "entidades_bancarias") // Nombre de la tabla
public class EntidadBancaria {

    @Id // El código de la entidad es la clave primaria
    private int codigo; // Identifica la entidad y debe ser positivo

    @Column(name = "domicilio_central", nullable = false)
    private String domicilioCentral; // Guarda la dirección de su sede central

    protected EntidadBancaria() {
        // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public EntidadBancaria(int codigo, String domicilioCentral) {
        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la entidad debe ser positivo."
            );
        }

        this.codigo = codigo;
        cambiarDomicilioCentral(domicilioCentral);
        // El constructor reutiliza la validación del domicilio
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDomicilioCentral() {
        return domicilioCentral;
    }

    public void cambiarDomicilioCentral(String domicilio) {

        // Rechaza valores nulos, vacíos o formados solo por espacios
        if (domicilio == null || domicilio.isBlank()) {
            throw new DatosInvalidosException(
                    "El domicilio central es obligatorio.");
        }

        // Respeta el tamaño de la columna en la base de datos
        if (domicilio.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El domicilio central no debe superar 255 caracteres.");
        }

        // Elimina espacios al principio y al final
        this.domicilioCentral = domicilio.trim();
    }
}