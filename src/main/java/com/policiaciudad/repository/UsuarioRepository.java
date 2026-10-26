package com.policiaciudad.repository;

import com.policiaciudad.modelo.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    // Busca la cuenta mediante su nombre de acceso
    Optional<Usuario> findByNombreUsuario(String nombreUsuario); //permitirá localizar la cuenta para autenticarla
//representa un resultado que puede contener un usuario o estar vacío si no existe.
    // Comprueba si el nombre de acceso ya está registrado
    boolean existsByNombreUsuario(String nombreUsuario); //exists permitirán comprobar duplicados antes de registrar cuentas.

    // Comprueba si la persona ya tiene una cuenta
    boolean existsByIdPersona(int idPersona);
}//Spring genera las consultas a partir de los nombres de los atributos de Usuario