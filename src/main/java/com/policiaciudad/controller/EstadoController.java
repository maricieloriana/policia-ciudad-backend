package com.policiaciudad.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //identifica una clase que recibe solicitudes web y devuelve respuestas
public class EstadoController {

    @GetMapping("/api/estado") //vincula una solicitud GET a esa dirección con nuestro método
    public String consultarEstado() { 
        return "Backend de Policia de la Ciudad funcionando";
    } //devuelve el texto de confirmación. Esta ruta solo comprueba el funcionamiento del backend
}
