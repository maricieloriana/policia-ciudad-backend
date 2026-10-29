package com.policiaciudad.repository;

import java.util.List;
import com.policiaciudad.modelo.Rol;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    boolean existsByNombreUsuarioAndIdNot(String nombreUsuario, Integer id);

    // Serializa las bajas y cambios de rol que afectan a los administradores.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Usuario u where u.rol = :rol order by u.id")
    List<Usuario> bloquearPorRol(@Param("rol") Rol rol);

}//Spring genera las consultas a partir de los nombres de los atributos de Usuario