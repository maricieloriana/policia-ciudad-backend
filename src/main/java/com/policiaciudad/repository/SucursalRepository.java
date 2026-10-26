package com.policiaciudad.repository;

import com.policiaciudad.modelo.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Administra sucursales cuyo identificador es el código
public interface SucursalRepository
        extends JpaRepository<Sucursal, Integer> {

    // Busca las sucursales que pertenecen a una entidad bancaria
    List<Sucursal> findByCodigoEntidadBancaria(int codigoEntidadBancaria);
    // Cuenta las sucursales que pertenecen a una entidad bancaria
    long countByCodigoEntidadBancaria(int codigoEntidadBancaria);
}