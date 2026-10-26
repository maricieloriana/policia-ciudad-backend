package com.policiaciudad.repository;

import com.policiaciudad.modelo.Vigilante;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra vigilantes mediante el identificador heredado de Persona
public interface VigilanteRepository
        extends JpaRepository<Vigilante, Integer> {

    // existsById se hereda de JpaRepository.
    // Devuelve true si existe un Vigilante con ese identificador.
}