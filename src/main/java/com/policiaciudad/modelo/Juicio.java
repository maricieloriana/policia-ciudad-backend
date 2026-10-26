package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Permite guardar juicios mediante JPA
@Table(name = "juicios") // Nombre de la tabla
public class Juicio {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera el ID
    private Integer id;

    @Column(name = "id_asalto", nullable = false, unique = true)
    private int idAsalto; // Relaciona el juicio con un asalto; solo admite un juicio por registro de asalto

    @Column(name = "id_persona_detenida", nullable = false)
    private int idPersonaDetenida; // Relaciona el juicio con la persona detenida

    @Column(name = "id_juez", nullable = false)
    private int idJuez; // Corresponde al identificador heredado de Persona

    @Column(nullable = false)
    private boolean condenado;

    @Column(name = "anios_carcel", nullable = false)
    private int aniosCarcel;

    protected Juicio() {
        // Constructor vacío necesario para que JPA reconstruya el objeto
    }

    public Juicio(int idAsalto, int idPersonaDetenida,
            int idJuez, boolean condenado, int aniosCarcel) {

        if (idAsalto <= 0 || idPersonaDetenida <= 0 || idJuez <= 0) {
            throw new DatosInvalidosException(
                    "Los identificadores deben ser positivos."
            );
        }

        this.idAsalto = idAsalto;
        this.idPersonaDetenida = idPersonaDetenida;
        this.idJuez = idJuez;

        if (condenado) {
            condenar(aniosCarcel);
        } else {
            if (aniosCarcel != 0) {
                throw new DatosInvalidosException(
                        "Una absolucion debe tener cero anios de carcel."
                );
            }

            absolver();
        }
    }

    public Integer getId() {
        return id;
    }

    public int getIdAsalto() {
        return idAsalto;
    }

    public int getIdPersonaDetenida() {
        return idPersonaDetenida;
    }

    public int getIdJuez() {
        return idJuez;
    }

    public boolean isCondenado() {
        return condenado;
    }

    public int getAniosCarcel() {
        return aniosCarcel;
    }

    public void condenar(int anios) { // Establece juntos el resultado y los años de cárcel
        if (anios <= 0) {
            throw new DatosInvalidosException(
                    "La condena debe ser mayor que cero."
            );
        }

        this.condenado = true;
        this.aniosCarcel = anios;
    }

    public void absolver() { // Establece absolución y pena cero
        this.condenado = false;
        this.aniosCarcel = 0;
    }
}