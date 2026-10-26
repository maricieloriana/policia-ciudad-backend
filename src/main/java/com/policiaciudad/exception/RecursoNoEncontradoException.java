package com.policiaciudad.exception;
//usaremos cuando no exista un registro solicitado o relacionado
//La utilizaremos en los servicios, al consultar los repositorios. Los modelos no consultan la base de datos
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje); //Recibe un mensaje que identifica el registro que no se encontró
    }
}