package com.policiaciudad.service;

import com.policiaciudad.dto.BandaDTO;
import java.util.List;

public interface BandaService {

    // Define la operación para registrar una banda
    BandaDTO registrar(Integer numero, Integer cantidadMiembros);
    // Define la consulta de las bandas registradas
    List<BandaDTO> listar();
    // Define la consulta de una banda por su número
    BandaDTO consultarPorNumero(int numero);
    // Define la actualización de la cantidad total de miembros
    BandaDTO actualizarCantidadMiembros(int numero, Integer cantidadMiembros);
    // Define la eliminación de una banda sin personas detenidas asociadas
    void eliminar(int numero);
}