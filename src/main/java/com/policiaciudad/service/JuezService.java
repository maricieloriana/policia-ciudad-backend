package com.policiaciudad.service;

import com.policiaciudad.dto.JuezDTO;
import java.util.List;

// Contrato de registro de jueces
public interface JuezService {

    JuezDTO registrar(
            String nombreCompleto,
            Integer claveInterna,
            Integer aniosServicio
    );
    // Define la consulta de los jueces registrados
    List<JuezDTO> listar();
    // Define la consulta de un juez por su identificador
    JuezDTO consultarPorId(int id);
    // Define la actualización de los años de servicio de un juez
    JuezDTO actualizarAniosServicio(int id, Integer aniosServicio);
    // Define la corrección del nombre completo de un juez
    JuezDTO actualizarNombre(int id, String nombreCompleto);
    // Define la eliminación de un juez sin registros que lo referencien
    void eliminar(int id);
    JuezDTO actualizarDatos(int id, String nombreCompleto, Integer aniosServicio);
}    //Devuelve un JuezDTO con los datos registrados