package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity // Permite guardar cuentas de acceso mediante JPA
@Table(name = "usuarios") // Nombre de la tabla
public class Usuario {

    @Id // Clave primaria de la cuenta
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera el ID
    private Integer id; // Identifica la cuenta

    @Column(name = "id_persona", nullable = false, unique = true)
    private int idPersona; // Identifica a su titular; cada persona puede tener como máximo una cuenta

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario; // El nombre de acceso no debe repetirse

    @Column(name = "password_hash", nullable = false)
    private String passwordHash; // Guarda el resultado de procesar la contraseña

    @Enumerated(EnumType.STRING) // Guarda el nombre del rol en lugar de su posición numérica
    @Column(nullable = false)
    private Rol rol;

    protected Usuario() {
        // Constructor necesario para JPA.
    }

    public Usuario(int idPersona, String nombreUsuario,
            String passwordHash, Rol rol) {
        this.idPersona = idPersona;
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
