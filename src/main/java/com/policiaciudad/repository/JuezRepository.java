package com.policiaciudad.repository;

import com.policiaciudad.modelo.Juez;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra jueces mediante el identificador heredado de Persona
public interface JuezRepository extends JpaRepository<Juez, Integer> {

    // Comprueba si la clave interna ya pertenece a un juez
    boolean existsByClaveInterna(int claveInterna);
}