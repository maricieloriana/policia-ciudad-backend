package com.policiaciudad.repository;

import com.policiaciudad.modelo.Juicio;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra los juicios mediante su identificador
public interface JuicioRepository extends JpaRepository<Juicio, Integer> {

    // Comprueba si el asalto ya tiene un juicio registrado
    boolean existsByIdAsalto(int idAsalto);
    // Comprueba si existen juicios asociados al juez
    boolean existsByIdJuez(int idJuez);
}