package com.policiaciudad.controller;

import com.policiaciudad.dto.RegistroAsaltoDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.service.AsaltoService;
import com.policiaciudad.service.PersonaDetenidaService;
import com.policiaciudad.service.SucursalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.policiaciudad.dto.ActualizarFechaAsaltoDTO;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.exception.RecursoEnUsoException;

@Controller
public class AsaltoWebController {

    private final AsaltoService servicio;
    private final SucursalService sucursalService;
    private final PersonaDetenidaService personaService;

    public AsaltoWebController(
            AsaltoService servicio,
            SucursalService sucursalService,
            PersonaDetenidaService personaService) {

        this.servicio = servicio;
        this.sucursalService = sucursalService;
        this.personaService = personaService;
    }

    @GetMapping("/asaltos")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("asaltos", servicio.listar());

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "asaltos";
    }

    @GetMapping("/asaltos/nuevo")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute("asalto", new RegistroAsaltoDTO());
        cargarOpciones(modelo);

        return "asalto-formulario";
    }

    @PostMapping("/asaltos")
    public String registrar(
            @ModelAttribute("asalto") RegistroAsaltoDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá la sucursal, la persona y la fecha.");

            cargarOpciones(modelo);
            return "asalto-formulario";
        }

        try {
            servicio.registrar(
                    datos.getCodigoSucursal(),
                    datos.getIdPersonaDetenida(),
                    datos.getFecha());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Asalto registrado correctamente.");

            return "redirect:/asaltos";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            cargarOpciones(modelo);

            return "asalto-formulario";
        }
    }

    private void cargarOpciones(Model modelo) {

        modelo.addAttribute("sucursales", sucursalService.listar());
        modelo.addAttribute("personas", personaService.listar());
    }
    
        @GetMapping("/asaltos/{id}/editar")
    public String mostrarEdicion(
            @PathVariable int id, Model modelo) {

        ActualizarFechaAsaltoDTO datos = new ActualizarFechaAsaltoDTO();
        datos.setFecha(servicio.consultarPorId(id).getFecha());

        modelo.addAttribute("asalto", datos);
        modelo.addAttribute("idAsalto", id);

        return "asalto-edicion";
    }

    @PostMapping("/asaltos/{id}/editar")
    public String actualizar(
            @PathVariable int id,
            @ModelAttribute("asalto") ActualizarFechaAsaltoDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("idAsalto", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisá la fecha ingresada.");
            return "asalto-edicion";
        }

        try {
            servicio.actualizarFecha(id, datos.getFecha());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Asalto actualizado correctamente.");

            return "redirect:/asaltos";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "asalto-edicion";
        }
    }
    
    @PostMapping("/asaltos/{id}/eliminar")
    public String eliminar(
            @PathVariable int id,
            RedirectAttributes mensajes) {

        try {
            servicio.eliminar(id);

            mensajes.addFlashAttribute(
                    "mensajeExito", "Asalto eliminado correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/asaltos";
    }
}