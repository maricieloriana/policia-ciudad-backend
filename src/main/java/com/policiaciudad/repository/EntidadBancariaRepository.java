package com.policiaciudad.repository;

import com.policiaciudad.modelo.EntidadBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntidadBancariaRepository
        extends JpaRepository<EntidadBancaria, Integer> {
}
