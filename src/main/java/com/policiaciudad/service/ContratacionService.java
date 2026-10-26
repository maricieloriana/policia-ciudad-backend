package com.policiaciudad.service;

import com.policiaciudad.dto.ContratacionDTO;
import java.util.List;
import java.time.LocalDate;

// Contrato de las operaciones de contratación
public interface ContratacionService {

    // Registra una contratación y devuelve los datos guardados
    ContratacionDTO registrar(ContratacionDTO datos);

    // Devuelve las contrataciones como una lista de DTO
    List<ContratacionDTO> listar();
    // Consulta las contrataciones del vigilante asociado a la cuenta autenticada
    List<ContratacionDTO> listarPropias(String nombreUsuario);
    // Define la consulta de una contratación por su identificador
    ContratacionDTO consultarPorId(int id);
    // Define la actualización de la condición de arma
    ContratacionDTO actualizarCondicionArma(int id, Boolean conArma);
    // Define la actualización de la fecha de una contratación
    ContratacionDTO actualizarFecha(int id, LocalDate fecha);
    // Define la eliminación de una contratación existente
    void eliminar(int id);
    // Actualiza la fecha y la condición de arma en una misma operación
    ContratacionDTO actualizarDatos(int id, LocalDate fecha, Boolean conArma);
}
