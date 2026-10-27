package com.policiaciudad.controller;

import com.policiaciudad.dto.RegistroBandaDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.service.BandaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.policiaciudad.dto.BandaDTO;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.exception.RecursoEnUsoException;

@Controller
public class BandaWebController {

    private final BandaService servicio;

    public BandaWebController(BandaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/bandas")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("bandas", servicio.listar());

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "bandas";
    }

    @GetMapping("/bandas/nueva")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute("banda", new RegistroBandaDTO());

        return "banda-formulario";
    }

    @PostMapping("/bandas")
    public String registrar(
            @ModelAttribute("banda") RegistroBandaDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el número y la cantidad de miembros.");

            return "banda-formulario";
        }

        try {
            servicio.registrar(
                    datos.getNumero(), datos.getCantidadMiembros());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Banda registrada correctamente.");

            return "redirect:/bandas";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());

            return "banda-formulario";
        }
    }
    
        @GetMapping("/bandas/{numero}/editar")
    public String mostrarEdicion(
            @PathVariable int numero, Model modelo) {

        BandaDTO actual = servicio.consultarPorNumero(numero);

        RegistroBandaDTO datos = new RegistroBandaDTO();
        datos.setNumero(actual.getNumero());
        datos.setCantidadMiembros(actual.getCantidadMiembros());

        modelo.addAttribute("banda", datos);
        modelo.addAttribute("numeroBanda", numero);

        return "banda-edicion";
    }

    @PostMapping("/bandas/{numero}/editar")
    public String actualizar(
            @PathVariable int numero,
            @ModelAttribute("banda") RegistroBandaDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("numeroBanda", numero);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá la cantidad de miembros ingresada.");

            return "banda-edicion";
        }

        try {
            servicio.actualizarCantidadMiembros(
                    numero, datos.getCantidadMiembros());

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Banda actualizada correctamente.");

            return "redirect:/bandas";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());

            return "banda-edicion";
        }
    }
    
    @PostMapping("/bandas/{numero}/eliminar")
    public String eliminar(
            @PathVariable int numero,
            RedirectAttributes mensajes) {

        try {
            servicio.eliminar(numero);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Banda eliminada correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/bandas";
    }
}