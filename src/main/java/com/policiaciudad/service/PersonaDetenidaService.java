package com.policiaciudad.service;

import com.policiaciudad.dto.PersonaDetenidaDTO;
import com.policiaciudad.dto.RegistroPersonaDetenidaDTO;
import java.util.List;

public interface PersonaDetenidaService {

    PersonaDetenidaDTO registrar(RegistroPersonaDetenidaDTO datos);
    List<PersonaDetenidaDTO> listar();
    PersonaDetenidaDTO consultarPorId(int id);
    PersonaDetenidaDTO actualizarDatos(int id, RegistroPersonaDetenidaDTO datos);
    void eliminar(int id);
    PersonaDetenidaDTO actualizarBanda(int id, Integer numeroBanda);
}
