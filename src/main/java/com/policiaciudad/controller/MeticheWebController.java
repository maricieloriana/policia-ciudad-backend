package com.policiaciudad.controller;

import com.policiaciudad.dto.CambioPasswordDTO;
import com.policiaciudad.dto.UsuarioDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.service.UsuarioService;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MeticheWebController {

    private final UsuarioService servicio;

    public MeticheWebController(UsuarioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/metiche")
    public String mostrarVista(Model modelo, Authentication autenticacion) {

        List<UsuarioDTO> usuarios = servicio.listar().stream()
                .filter(usuario -> !usuario.getNombreUsuario()
                .equals(autenticacion.getName()))
                .toList();

        modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("datos", new CambioPasswordDTO());

        return "metiche";
    }

    @PostMapping("/metiche/usuarios/{id}/password")
    public String cambiarPassword(
            @PathVariable int id,
            @ModelAttribute("datos") CambioPasswordDTO datos,
            Authentication autenticacion,
            RedirectAttributes mensajes) {

        try {
            servicio.cambiarPassword(
                    id,
                    datos.getNuevaPassword(),
                    autenticacion.getName()
            );

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "La contraseña se cambió correctamente."
            );

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | AccessDeniedException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError",
                    excepcion.getMessage()
            );
        }

        return "redirect:/metiche";
    }
}