package com.policiaciudad.dto;

import java.util.List;

public class EntidadBancariaDTO {

    private int codigo;
    private String domicilioCentral;
    private List<SucursalDTO> sucursales;

    // Prepara la respuesta con la entidad y sus sucursales
    public EntidadBancariaDTO(int codigo, String domicilioCentral,
            List<SucursalDTO> sucursales) {

        this.codigo = codigo;
        this.domicilioCentral = domicilioCentral;
        this.sucursales = sucursales;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDomicilioCentral() {
        return domicilioCentral;
    }

    public List<SucursalDTO> getSucursales() {
        return sucursales;
    }
}
