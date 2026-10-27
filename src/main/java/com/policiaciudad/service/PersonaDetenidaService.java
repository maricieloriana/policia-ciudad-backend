package com.policiaciudad.service;

import com.policiaciudad.dto.PersonaDetenidaDTO;
import java.util.List;

// Contrato de registro de personas detenidas
public interface PersonaDetenidaService {

    PersonaDetenidaDTO registrar(
            String nombreCompleto,
            Integer numeroBanda
    );
    // Define la consulta de las personas detenidas registradas
    List<PersonaDetenidaDTO> listar();
    // Define la consulta de una persona detenida por su identificador
    PersonaDetenidaDTO consultarPorId(int id);
    // Define la asignación o eliminación de la banda
    PersonaDetenidaDTO actualizarBanda(int id, Integer numeroBanda);
    // Define la corrección del nombre completo de una persona detenida
    PersonaDetenidaDTO actualizarNombre(int id, String nombreCompleto);
    void eliminar(int identificador);
}
