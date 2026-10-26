package com.policiaciudad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//para mostrar una plantilla HTML
public class AccesoController {

    @GetMapping("/login")
    public String mostrarLogin() {

        // Abre la plantilla templates/login.html
        return "login";//indica el nombre del archivo que Thymeleaf debe abrir
    }
}