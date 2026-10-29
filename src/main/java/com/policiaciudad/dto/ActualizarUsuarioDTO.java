package com.policiaciudad.dto;

import com.policiaciudad.modelo.Rol;

// La edicion no permite cambiar el titular ni recibir una contrasenia.
public class ActualizarUsuarioDTO {
    private String nombreUsuario;
    private Rol rol;

    public ActualizarUsuarioDTO() { }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
