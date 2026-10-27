package com.policiaciudad.controller;

import com.policiaciudad.dto.RegistroJuezDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.service.JuezService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.policiaciudad.dto.JuezDTO;

@Controller
public class JuezWebController {

    private final JuezService servicio;

    public JuezWebController(JuezService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/jueces")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("jueces", servicio.listar());

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "jueces";
    }

    @GetMapping("/jueces/nuevo")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute("juez", new RegistroJuezDTO());

        return "juez-formulario";
    }

    @PostMapping("/jueces")
    public String registrar(
            @ModelAttribute("juez") RegistroJuezDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el nombre, la clave y los años de servicio.");

            return "juez-formulario";
        }

        try {
            servicio.registrar(
                    datos.getNombreCompleto(),
                    datos.getClaveInterna(),
                    datos.getAniosServicio());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Juez registrado correctamente.");

            return "redirect:/jueces";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "juez-formulario";
        }
    }

    @PostMapping("/jueces/{id}/eliminar")
    public String eliminar(
            @PathVariable int id,
            RedirectAttributes mensajes) {

        try {
            servicio.eliminar(id);

            mensajes.addFlashAttribute(
                    "mensajeExito", "Juez eliminado correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/jueces";
    }
    
    @GetMapping("/jueces/{id}/editar")
    public String mostrarEdicion(
            @PathVariable int id, Model modelo) {

        JuezDTO actual = servicio.consultarPorId(id);

        RegistroJuezDTO datos = new RegistroJuezDTO();
        datos.setNombreCompleto(actual.getNombreCompleto());
        datos.setAniosServicio(actual.getAniosServicio());

        modelo.addAttribute("juez", datos);
        modelo.addAttribute("idJuez", id);

        return "juez-edicion";
    }

    @PostMapping("/jueces/{id}/editar")
    public String actualizar(
            @PathVariable int id,
            @ModelAttribute("juez") RegistroJuezDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("idJuez", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el nombre y los años de servicio.");

            return "juez-edicion";
        }

        try {
            servicio.actualizarDatos(
                    id,
                    datos.getNombreCompleto(),
                    datos.getAniosServicio());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Juez actualizado correctamente.");

            return "redirect:/jueces";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "juez-edicion";
        }
    }
}