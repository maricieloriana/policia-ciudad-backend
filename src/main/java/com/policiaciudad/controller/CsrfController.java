package com.policiaciudad.controller;

import java.util.Map;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Permite devolver los datos del token como JSON
public class CsrfController {
//Spring proporciona el objeto CsrfToken; no generamos el token manualmente.
    @GetMapping("/api/csrf")
    public Map<String, String> consultarToken(CsrfToken token) {
//mapstring contiene pares de claves y valores de texto
        return Map.of(
                "headerName", token.getHeaderName(), //indica el nombre de la cabecera donde enviaremos el token
                "token", token.getToken() //contiene su valor
        );
    }
}