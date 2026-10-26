package com.policiaciudad.dto;

import com.policiaciudad.modelo.Rol;

public class UsuarioDTO {

    private Integer id;
    private int idPersona;
    private String nombreUsuario;
    private Rol rol;

    public UsuarioDTO(Integer id, int idPersona,
            String nombreUsuario, Rol rol) {

        this.id = id;
        this.idPersona = idPersona;
        this.nombreUsuario = nombreUsuario;
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

    public Rol getRol() {
        return rol;
    }
} //El constructor recibe los datos de la cuenta guardada.
//Los getters permiten convertir esos datos a JSON.
//No contiene contraseña ni hash, porque esos datos no corresponden a la respuesta.
//Usamos RegistroUsuarioDTO para la entrada y UsuarioDTO para la salida, porque transportan información diferente.