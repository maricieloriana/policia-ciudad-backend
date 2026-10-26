package com.policiaciudad.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.policiaciudad.modelo.Rol;

public class RegistroUsuarioDTO {

    private int idPersona;
    private String nombreUsuario;

    // Permite recibir la contraseña, pero la excluye al generar JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //writeonly permite recibir la contraseña desde JSON y evita incluirla al convertir el DTO a JSON.
    private String contrasenia;

    private Rol rol;

    public RegistroUsuarioDTO() {
        // Permite cargar los datos recibidos en la solicitud
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() { //El servicio puede leerla mediante getContrasenia() para generar el hash
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
} //Transporta el titular, el nombre de acceso, la contraseña y el rol solicitados.
//El DTO no se guarda en la base: el servicio utiliza estos datos para construir Usuario.
//El rol recibido será validado por el servicio. La ruta permitirá registrar cuentas únicamente al administrador.