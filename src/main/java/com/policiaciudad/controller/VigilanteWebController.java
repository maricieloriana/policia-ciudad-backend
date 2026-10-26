package com.policiaciudad.controller;

import com.policiaciudad.service.VigilanteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.policiaciudad.dto.RegistroVigilanteDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import com.policiaciudad.dto.VigilanteDTO;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VigilanteWebController {

    private final VigilanteService servicio;

    public VigilanteWebController(VigilanteService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/vigilantes")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("vigilantes", servicio.listar());

        // Permite mostrar las acciones del administrador
        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "vigilantes";
    }
    
    @GetMapping("/vigilantes/nuevo")
    public String mostrarFormulario(Model modelo) {

        // Prepara un objeto vacío para recibir los datos del formulario
        modelo.addAttribute("vigilante", new RegistroVigilanteDTO());

        return "vigilante-formulario";
    }
    
    @PostMapping("/vigilantes")
    public String registrar(
            @ModelAttribute("vigilante") RegistroVigilanteDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        // Detecta errores al convertir los campos del formulario
        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisa el nombre y la edad ingresados.");
            return "vigilante-formulario";
        }

        try {
            // Reutiliza las validaciones y el guardado del servicio
            servicio.registrar(datos.getNombreCompleto(), datos.getEdad());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Vigilante registrado correctamente.");

            // Redirige para evitar repetir el registro al actualizar la página
            return "redirect:/vigilantes";

        } catch (DatosInvalidosException excepcion) {
            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "vigilante-formulario";
        }
    }
    
    @GetMapping("/vigilantes/{id}/editar")
    public String mostrarEdicion(@PathVariable int id, Model modelo) {

        // Consulta los datos actuales del vigilante
        VigilanteDTO actual = servicio.consultarPorId(id);

        // Carga esos datos en el objeto del formulario
        RegistroVigilanteDTO datos = new RegistroVigilanteDTO();
        datos.setNombreCompleto(actual.getNombreCompleto());
        datos.setEdad(actual.getEdad());

        modelo.addAttribute("vigilante", datos);
        modelo.addAttribute("idVigilante", id);

        // Reutiliza la plantilla de registro
        return "vigilante-formulario";
    }
    
    @PostMapping("/vigilantes/{id}/editar")
    public String actualizar(
            @PathVariable int id,
            @ModelAttribute("vigilante") RegistroVigilanteDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        // Conserva el modo de edición si hay errores
        modelo.addAttribute("idVigilante", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisa el nombre y la edad ingresados.");
            return "vigilante-formulario";
        }

        try {
            // Guarda ambos datos dentro de una misma transacción
            servicio.actualizarDatos(
                    id,
                    datos.getNombreCompleto(),
                    datos.getEdad());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Vigilante actualizado correctamente.");

            return "redirect:/vigilantes";

        } catch (DatosInvalidosException excepcion) {
            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "vigilante-formulario";
        }
    }
}