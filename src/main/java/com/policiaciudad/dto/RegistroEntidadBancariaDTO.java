package com.policiaciudad.dto;

import java.util.List;

public class RegistroEntidadBancariaDTO {

    private Integer codigo;
    private String domicilioCentral;

    // Sucursales que se registrarán junto con la entidad
    private List<RegistroSucursalDTO> sucursales;

    public RegistroEntidadBancariaDTO() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDomicilioCentral() {
        return domicilioCentral;
    }

    public void setDomicilioCentral(String domicilioCentral) {
        this.domicilioCentral = domicilioCentral;
    }

    public List<RegistroSucursalDTO> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<RegistroSucursalDTO> sucursales) {
        this.sucursales = sucursales;
    }
}
