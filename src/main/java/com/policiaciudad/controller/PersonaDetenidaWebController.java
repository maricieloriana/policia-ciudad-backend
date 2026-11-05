package com.policiaciudad.controller;

import com.policiaciudad.dto.RegistroPersonaDetenidaDTO;
import com.policiaciudad.service.BandaService;
import com.policiaciudad.service.PersonaDetenidaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.policiaciudad.dto.PersonaDetenidaDTO;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarNombreDTO;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.exception.RegistroDuplicadoException;

@Controller
public class PersonaDetenidaWebController {

    private final PersonaDetenidaService servicio;
    private final BandaService bandaService;

    public PersonaDetenidaWebController(
            PersonaDetenidaService servicio,
            BandaService bandaService) {

        this.servicio = servicio;
        this.bandaService = bandaService;
    }

    @GetMapping("/personas-detenidas")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("personasDetenidas", servicio.listar());

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "personas-detenidas";
    }

    @GetMapping("/personas-detenidas/nueva")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute(
                "persona", new RegistroPersonaDetenidaDTO());

        modelo.addAttribute("bandas", bandaService.listar());

        return "persona-detenida-formulario";
    }
    
    @PostMapping("/personas-detenidas")
    public String registrar(
            @ModelAttribute("persona") RegistroPersonaDetenidaDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá los datos personales, la fecha y la banda.");
            modelo.addAttribute("bandas", bandaService.listar());

            return "persona-detenida-formulario";
        }

        try {
            servicio.registrar(datos);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Persona detenida registrada correctamente.");

            return "redirect:/personas-detenidas";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            modelo.addAttribute("bandas", bandaService.listar());

            return "persona-detenida-formulario";
        }
    }
        @GetMapping("/personas-detenidas/{id}/banda")
    public String mostrarCambioBanda(
            @PathVariable int id, Model modelo) {

        PersonaDetenidaDTO actual = servicio.consultarPorId(id);

        RegistroPersonaDetenidaDTO datos =
                new RegistroPersonaDetenidaDTO();

        datos.setNumeroBanda(actual.getNumeroBanda());

        modelo.addAttribute("persona", datos);
        modelo.addAttribute("idPersona", id);
        modelo.addAttribute("bandas", bandaService.listar());

        return "persona-detenida-banda";
    }

    @PostMapping("/personas-detenidas/{id}/banda")
    public String actualizarBanda(
            @PathVariable int id,
            @ModelAttribute("persona") RegistroPersonaDetenidaDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("idPersona", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisá la banda seleccionada.");
            modelo.addAttribute("bandas", bandaService.listar());
            return "persona-detenida-banda";
        }

        try {
            servicio.actualizarBanda(id, datos.getNumeroBanda());

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Banda de la persona actualizada correctamente.");

            return "redirect:/personas-detenidas";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            modelo.addAttribute("bandas", bandaService.listar());
            return "persona-detenida-banda";
        }
    }
    
    @GetMapping("/personas-detenidas/{id}/nombre")
    public String mostrarCambioNombre(
            @PathVariable int id, Model modelo) {

        PersonaDetenidaDTO actual = servicio.consultarPorId(id);

        RegistroPersonaDetenidaDTO datos
                = new RegistroPersonaDetenidaDTO();

        datos.setNombre(actual.getNombre());
        datos.setApellido(actual.getApellido());
        datos.setDni(actual.getDni());
        datos.setFechaNacimiento(actual.getFechaNacimiento());
        datos.setMail(actual.getMail());
        datos.setTelefono(actual.getTelefono());

        modelo.addAttribute("persona", datos);
        modelo.addAttribute("idPersona", id);

        return "persona-detenida-nombre";
    }

    @PostMapping("/personas-detenidas/{id}/nombre")
    public String actualizarNombre(
            @PathVariable int id,
            @ModelAttribute("persona") RegistroPersonaDetenidaDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("idPersona", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá los datos personales y el formato de la fecha.");

            return "persona-detenida-nombre";
        }

        try {
            servicio.actualizarDatos(id, datos);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Datos personales actualizados correctamente.");

            return "redirect:/personas-detenidas";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());

            return "persona-detenida-nombre";
        }
    }
   
    @PostMapping("/personas-detenidas/{id}/eliminar")
    public String eliminar(
            @PathVariable int id,
            RedirectAttributes mensajes) {

        try {
            servicio.eliminar(id);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Persona detenida eliminada correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/personas-detenidas";
    }
}
