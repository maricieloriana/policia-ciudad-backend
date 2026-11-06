package com.policiaciudad.dto;

public class CambioPasswordDTO {

    private String nuevaPassword;

    public CambioPasswordDTO() {
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }
}