package com.policiaciudad.service;

import com.policiaciudad.dto.VigilanteDTO;
import java.util.List;

// Contrato para consultar los datos del vigilante autenticado
public interface VigilanteService {

    VigilanteDTO consultarPropio(String nombreUsuario); //recibirá el nombre de la cuenta desde la autenticación.
 //La implementación buscará al titular y devolverá su identificador, nombre y edad mediante VigilanteDTO.
//El cliente no elegirá el ID de la persona consultada.
//La interfaz declara el contrato;
    // Registra los datos personales y propios de un vigilante
    // Integer permite detectar una edad no informada
    VigilanteDTO registrar(String nombreCompleto, Integer edad);
    // Define la consulta de todos los vigilantes registrados
    List<VigilanteDTO> listar();
    // Define la consulta de un vigilante por su identificador
    VigilanteDTO consultarPorId(int id);
    // Define la actualización de la edad de un vigilante
    VigilanteDTO actualizarEdad(int id, Integer edad);
    // Define la corrección del nombre completo de un vigilante
    VigilanteDTO actualizarNombre(int id, String nombreCompleto);
    // Define la eliminación de un vigilante sin registros que lo referencien
    void eliminar(int id);
    // Actualiza juntos el nombre completo y la edad
    VigilanteDTO actualizarDatos(int id, String nombreCompleto, Integer edad);
}