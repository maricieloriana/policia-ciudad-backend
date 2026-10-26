package com.policiaciudad.repository;

import com.policiaciudad.modelo.PersonaDetenida;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra personas detenidas mediante el ID heredado de Persona
public interface PersonaDetenidaRepository
        extends JpaRepository<PersonaDetenida, Integer> {

    // Comprueba si alguna persona detenida pertenece a la banda
    boolean existsByNumeroBanda(int numeroBanda);
}// save, findById y existsById se heredan de JpaRepository