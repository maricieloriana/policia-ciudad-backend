package com.policiaciudad.controller;

import com.policiaciudad.dto.RegistroUsuarioDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioWebController {

    private final UsuarioService servicio;

    public UsuarioWebController(UsuarioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/usuarios/nuevo")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute("usuario", new RegistroUsuarioDTO());
        modelo.addAttribute("roles", Rol.values());

        return "usuario-formulario";
    }

    @PostMapping("/usuarios")
    public String registrar(
            @ModelAttribute("usuario") RegistroUsuarioDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            datos.setContrasenia(null);
            modelo.addAttribute("roles", Rol.values());
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el ID de persona y el rol. Volvé a ingresar la contraseña.");

            return "usuario-formulario";
        }

        try {
            servicio.registrar(
                    datos.getIdPersona(),
                    datos.getNombreUsuario(),
                    datos.getContrasenia(),
                    datos.getRol());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Cuenta de usuario registrada correctamente.");

            return "redirect:/usuarios/nuevo";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RegistroDuplicadoException excepcion) {

            datos.setContrasenia(null);
            modelo.addAttribute("roles", Rol.values());
            modelo.addAttribute(
                    "mensajeError",
                    excepcion.getMessage()
                    + " Volvé a ingresar la contraseña.");

            return "usuario-formulario";
        }
    }
}