package com.policiaciudad.service;

import com.policiaciudad.dto.JuezDTO;
import com.policiaciudad.dto.RegistroJuezDTO;
import java.util.List;

public interface JuezService {

    JuezDTO registrar(RegistroJuezDTO datos);
    List<JuezDTO> listar();
    JuezDTO consultarPorId(int id);
    JuezDTO actualizarDatos(int id, RegistroJuezDTO datos);
    void eliminar(int id);
    JuezDTO actualizarAniosServicio(int id, Integer aniosServicio);
}
