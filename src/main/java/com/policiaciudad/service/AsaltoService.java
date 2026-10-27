package com.policiaciudad.service;

import com.policiaciudad.dto.AsaltoDTO;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;

// Contrato de registro de asaltos
public interface AsaltoService {

    AsaltoDTO registrar(
            Integer codigoSucursal,
            Integer idPersonaDetenida,
            LocalDate fecha
    );
    // Devuelve los asaltos registrados
    List<AsaltoDTO> listar();
    // Define la consulta de un asalto por su identificador
    AsaltoDTO consultarPorId(int id);
    // Define la corrección de la fecha de un asalto
    AsaltoDTO actualizarFecha(int id, LocalDate fecha);
    void eliminar(int id);
}