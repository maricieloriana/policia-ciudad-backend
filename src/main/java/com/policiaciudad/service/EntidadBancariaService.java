package com.policiaciudad.service;

import com.policiaciudad.dto.EntidadBancariaDTO;
import com.policiaciudad.dto.RegistroEntidadBancariaDTO;
import java.util.List;

public interface EntidadBancariaService {

    // Define el registro de una entidad junto con sus sucursales
    EntidadBancariaDTO registrar(RegistroEntidadBancariaDTO datos);
    // Define la consulta de entidades con sus sucursales
    List<EntidadBancariaDTO> listar();
    // Define la consulta de una entidad bancaria por su código
    EntidadBancariaDTO consultarPorCodigo(int codigo);
    // Define la corrección del domicilio central de una entidad bancaria
    EntidadBancariaDTO actualizarDomicilioCentral(int codigo, String domicilioCentral);
    void eliminar(int identificador);
}