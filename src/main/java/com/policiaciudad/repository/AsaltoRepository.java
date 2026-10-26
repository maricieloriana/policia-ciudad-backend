package com.policiaciudad.repository;

import com.policiaciudad.modelo.Asalto;
import org.springframework.data.jpa.repository.JpaRepository;

// Administra los registros de asaltos mediante su identificador
public interface AsaltoRepository extends JpaRepository<Asalto, Integer> {
    // Comprueba si la sucursal tiene asaltos registrados
    boolean existsByCodigoSucursal(int codigoSucursal);
}// save, findById y existsById se heredan de JpaRepository