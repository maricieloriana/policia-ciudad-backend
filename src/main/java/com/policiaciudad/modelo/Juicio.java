package com.policiaciudad.modelo;

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
        // Constructor necesario para JPA.
    }

    public Juicio(int idAsalto, int idPersonaDetenida, int idJuez,
            boolean condenado, int aniosCarcel) {
        this.idAsalto = idAsalto;
        this.idPersonaDetenida = idPersonaDetenida;
        this.idJuez = idJuez;
        this.condenado = condenado;
        this.aniosCarcel = aniosCarcel;
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

    public void setCondenado(boolean condenado) {
        this.condenado = condenado;
    }

    public int getAniosCarcel() {
        return aniosCarcel;
    }

    public void setAniosCarcel(int aniosCarcel) {
        this.aniosCarcel = aniosCarcel;
    }
}
