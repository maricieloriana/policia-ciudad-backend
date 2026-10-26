package com.policiaciudad.dto;
//DTO significa objeto de transferencia de datos
public class ErrorDTO {

    private int estado; //contiene el código HTTP del error: 400, 404 o 409, según corresponda
    private String mensaje;  //contiene la explicación del error

    public ErrorDTO(int estado, String mensaje) { //recibe esos valores y los guarda
        this.estado = estado;
        this.mensaje = mensaje;
    }
//Los getters permiten que Spring lea los datos para convertirlos en una respuesta JSON
    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
//Esta clase transporta información