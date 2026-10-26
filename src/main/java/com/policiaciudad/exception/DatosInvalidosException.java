package com.policiaciudad.exception;

public class DatosInvalidosException extends RuntimeException { //convierte nuestra clase en una excepción

    public DatosInvalidosException(String mensaje) { //recibe el mensaje que explica qué dato es inválido
        super(mensaje); //entrega ese mensaje al constructor de la excepción base
    }
}