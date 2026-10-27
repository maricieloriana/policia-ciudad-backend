package com.policiaciudad.service;

import com.policiaciudad.dto.JuicioDTO;
import java.util.List;

// Contrato para registrar resoluciones judiciales
public interface JuicioService {

    JuicioDTO registrar(
            Integer idAsalto,
            Integer idPersonaDetenida,
            Integer idJuez,
            Boolean condenado,
            Integer aniosCarcel
    );
    // Devuelve los juicios registrados
    List<JuicioDTO> listar();
    // Define la consulta de un juicio por su identificador
    JuicioDTO consultarPorId(int id);
    // Define la actualización de la resolución de un juicio
    JuicioDTO actualizarResolucion(int id, Boolean condenado, Integer aniosCarcel);
    void eliminar(int id);
}