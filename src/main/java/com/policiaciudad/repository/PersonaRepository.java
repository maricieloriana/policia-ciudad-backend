package com.policiaciudad.repository;

import com.policiaciudad.modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra los datos comunes de las personas utlizando su id
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
    boolean existsByDni(String dni); //devuelve true si alguna persona ya tiene ese DNI

    boolean existsByDniAndIdNot(String dni, Integer id);//comprueba si otra persona tiene ese DNI, excluyendo la que estamos editando

    // save y findById se heredan de JpaRepository
}//Hereda save para guardar al titular y obtener su ID generado.
//Ese ID se utilizará en Usuario.idPersona para vincular la cuenta.