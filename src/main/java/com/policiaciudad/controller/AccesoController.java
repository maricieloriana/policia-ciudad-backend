package com.policiaciudad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller//para mostrar una plantilla HTML
public class AccesoController {

    @GetMapping("/login")
    public String mostrarLogin() {

        // Abre la plantilla templates/login.html
        return "login";//indica el nombre del archivo que Thymeleaf debe abrir
    }
    
        // Permite obtener un token vigente para cerrar sesión mediante POST
    @GetMapping("/sesion/csrf")
    @ResponseBody
    public Map<String, String> obtenerToken(CsrfToken token) {

        return Map.of(
                "headerName", token.getHeaderName(),
                "token", token.getToken());
    }
}