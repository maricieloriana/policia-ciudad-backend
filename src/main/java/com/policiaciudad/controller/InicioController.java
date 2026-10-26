package com.policiaciudad.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/inicio")
    public String mostrarInicio(
            Authentication autenticacion, Model modelo) { //model lleva los datos del controlador al HTML

        // Envía el nombre del usuario autenticado a la pantalla
        modelo.addAttribute("nombreUsuario", autenticacion.getName());

        // Indica qué rol tiene para mostrar las opciones correspondientes
        modelo.addAttribute("esAdministrador", tieneRol(
                autenticacion, "ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esInvestigador", tieneRol(
                autenticacion, "ROLE_INVESTIGADOR"));

        modelo.addAttribute("esVigilante", tieneRol(
                autenticacion, "ROLE_VIGILANTE"));

        // Abre la plantilla templates/inicio.html
        return "inicio";
    }

    private boolean tieneRol(
            Authentication autenticacion, String rol) {

        return autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority().equals(rol));
    }
}