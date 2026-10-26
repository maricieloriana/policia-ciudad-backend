package com.policiaciudad.repository;

import com.policiaciudad.modelo.Banda;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra bandas utilizando su número como identificador
public interface BandaRepository extends JpaRepository<Banda, Integer> {

    // existsById comprueba si existe el número de banda recibido
    // save y findById también se heredan de JpaRepository
}