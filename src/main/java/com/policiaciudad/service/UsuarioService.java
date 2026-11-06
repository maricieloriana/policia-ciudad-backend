package com.policiaciudad.service;

import java.util.List;
import com.policiaciudad.dto.UsuarioDTO;

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

    List<UsuarioDTO> listar();
    UsuarioDTO consultarPorId(int id);
    UsuarioDTO actualizar(int id, String nombreUsuario, Rol rol,
            String nombreAdministrador);
    void eliminar(int id, String nombreAdministrador);
    
    void cambiarPassword(int id, String nuevaPassword, String nombreUsuarioSolicitante);

}