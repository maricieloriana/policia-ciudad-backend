package com.policiaciudad.service;

import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;

// Contrato para registrar cuentas vinculadas con personas existentes
//Recibe el identificador del titular, el nombre de acceso, la contraseña y el rol
public interface UsuarioService {

    Usuario registrar(
            int idPersona,
            String nombreUsuario,
            String contrasenia,
            Rol rol
    ); //Devuelve el Usuario registrado
}