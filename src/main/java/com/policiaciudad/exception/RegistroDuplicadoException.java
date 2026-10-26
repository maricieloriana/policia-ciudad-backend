package com.policiaciudad.exception;
//Los servicios la utilizarán al comprobar las reglas de unicidad
public class RegistroDuplicadoException extends RuntimeException {

    public RegistroDuplicadoException(String mensaje) {
        super(mensaje);  //recibe el mensaje que explica qué registro está duplicado
    }
}
