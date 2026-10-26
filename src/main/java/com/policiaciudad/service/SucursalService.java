package com.policiaciudad.service;

import com.policiaciudad.dto.SucursalDTO;
import java.util.List;
import com.policiaciudad.dto.RegistroSucursalDTO;

public interface SucursalService {

    // Define la consulta de todas las sucursales registradas
    List<SucursalDTO> listar();
    // Define la consulta de una sucursal por su código
    SucursalDTO consultarPorCodigo(int codigo);
    // Define la actualización de la cantidad de empleados
    SucursalDTO actualizarCantidadEmpleados(int codigo, Integer cantidadEmpleados);
    // Define la corrección del domicilio de una sucursal
    SucursalDTO actualizarDomicilio(int codigo, String domicilio);
    // Define el registro de una sucursal para una entidad existente
    SucursalDTO registrar(int codigoEntidadBancaria, RegistroSucursalDTO datos);
    // Define la eliminación de una sucursal respetando sus restricciones
    void eliminar(int codigo);
}
