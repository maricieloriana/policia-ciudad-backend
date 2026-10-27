package com.policiaciudad.controller;

import com.policiaciudad.dto.AsaltoDTO;
import com.policiaciudad.dto.RegistroJuicioDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.service.AsaltoService;
import com.policiaciudad.service.JuezService;
import com.policiaciudad.service.JuicioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.policiaciudad.dto.JuicioDTO;
import com.policiaciudad.dto.ActualizarResolucionJuicioDTO;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class JuicioWebController {

    private final JuicioService servicio;
    private final AsaltoService asaltoService;
    private final JuezService juezService;

    public JuicioWebController(
            JuicioService servicio,
            AsaltoService asaltoService,
            JuezService juezService) {

        this.servicio = servicio;
        this.asaltoService = asaltoService;
        this.juezService = juezService;
    }

    @GetMapping("/juicios")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("juicios", servicio.listar());

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "juicios";
    }

    @GetMapping("/juicios/nuevo")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute("juicio", new RegistroJuicioDTO());
        cargarOpciones(modelo);

        return "juicio-formulario";
    }

    @PostMapping("/juicios")
    public String registrar(
            @ModelAttribute("juicio") RegistroJuicioDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisá los datos ingresados.");
            cargarOpciones(modelo);
            return "juicio-formulario";
        }

        try {
            if (datos.getIdAsalto() == null) {
                throw new DatosInvalidosException(
                        "Seleccioná un asalto.");
            }

            // Obtiene la persona que corresponde al asalto seleccionado
            AsaltoDTO asalto =
                    asaltoService.consultarPorId(datos.getIdAsalto());

            servicio.registrar(
                    asalto.getId(),
                    asalto.getIdPersonaDetenida(),
                    datos.getIdJuez(),
                    datos.getCondenado(),
                    datos.getAniosCarcel());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Juicio registrado correctamente.");

            return "redirect:/juicios";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            cargarOpciones(modelo);
            return "juicio-formulario";
        }
    }

    private void cargarOpciones(Model modelo) {

        modelo.addAttribute("asaltos", asaltoService.listar());
        modelo.addAttribute("jueces", juezService.listar());
    }
        @GetMapping("/juicios/{id}/editar")
    public String mostrarEdicion(
            @PathVariable int id, Model modelo) {

        JuicioDTO actual = servicio.consultarPorId(id);

        ActualizarResolucionJuicioDTO datos =
                new ActualizarResolucionJuicioDTO();

        datos.setCondenado(actual.isCondenado());
        datos.setAniosCarcel(actual.getAniosCarcel());

        modelo.addAttribute("juicio", datos);
        modelo.addAttribute("idJuicio", id);

        return "juicio-edicion";
    }

    @PostMapping("/juicios/{id}/editar")
    public String actualizar(
            @PathVariable int id,
            @ModelAttribute("juicio") ActualizarResolucionJuicioDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("idJuicio", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá la resolución y los años de cárcel.");

            return "juicio-edicion";
        }

        try {
            servicio.actualizarResolucion(
                    id,
                    datos.getCondenado(),
                    datos.getAniosCarcel());

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Resolución del juicio actualizada correctamente.");

            return "redirect:/juicios";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "juicio-edicion";
        }
    }
    @PostMapping("/juicios/{id}/eliminar")
    public String eliminar(
            @PathVariable int id,
            RedirectAttributes mensajes) {

        try {
            servicio.eliminar(id);

            mensajes.addFlashAttribute(
                    "mensajeExito", "Juicio eliminado correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/juicios";
    }
}