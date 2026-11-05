package com.policiaciudad.service;

import com.policiaciudad.dto.VigilanteDTO;
import com.policiaciudad.dto.RegistroVigilanteDTO;
import java.util.List;

public interface VigilanteService {

    VigilanteDTO registrar(RegistroVigilanteDTO datos);
    List<VigilanteDTO> listar();
    VigilanteDTO consultarPorId(int id);
    VigilanteDTO actualizarDatos(int id, RegistroVigilanteDTO datos);
    void eliminar(int id);
    VigilanteDTO consultarPropio(String nombreUsuario);
}
