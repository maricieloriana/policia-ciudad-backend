package com.policiaciudad.exception;

import com.policiaciudad.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //registra el manejo centralizado de errores de los controladores
public class ManejadorGlobalErrores {

    @ExceptionHandler(DatosInvalidosException.class) //indica qué excepción atiende cada método. Spring selecciona el método correspondiente cuando esa excepción aparece durante una solicitud
    public ResponseEntity<ErrorDTO> manejarDatosInvalidos(
            DatosInvalidosException e) {

        ErrorDTO error = new ErrorDTO( //HttpStatus: contiene los códigos HTTP; .value() obtiene su número
                HttpStatus.BAD_REQUEST.value(), 
                e.getMessage() //obtiene el mensaje que pasamos al crear la excepción
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) 
                .body(error);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorDTO> manejarNoEncontrado( //permite devolver el estado HTTP y el objeto con los datos del error
            RecursoNoEncontradoException e) {

        ErrorDTO error = new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //establece el estado real de la respuesta
                .body(error); //coloca el DTO en su cuerpo
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErrorDTO> manejarDuplicado(
            RegistroDuplicadoException e) {

        ErrorDTO error = new ErrorDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    @ExceptionHandler(RecursoEnUsoException.class)
    public ResponseEntity<ErrorDTO> manejarRecursoEnUso(
            RecursoEnUsoException excepcion) {

        // Informa que otros registros utilizan el recurso
        ErrorDTO error = new ErrorDTO(
                HttpStatus.CONFLICT.value(),
                excepcion.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}