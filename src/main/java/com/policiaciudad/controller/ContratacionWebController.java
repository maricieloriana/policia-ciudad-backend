package com.policiaciudad.controller;

import com.policiaciudad.dto.ContratacionDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.service.ContratacionService;
import com.policiaciudad.service.SucursalService;
import com.policiaciudad.service.VigilanteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ContratacionWebController {

    private final ContratacionService servicio;
    private final SucursalService sucursalService;
    private final VigilanteService vigilanteService;

    public ContratacionWebController(
            ContratacionService servicio,
            SucursalService sucursalService,
            VigilanteService vigilanteService) {

        this.servicio = servicio;
        this.sucursalService = sucursalService;
        this.vigilanteService = vigilanteService;
    }

    @GetMapping("/contrataciones")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("contrataciones", servicio.listar());

        // Permite mostrar la opción de registro al administrador
        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);
        
        // Título de la consulta general
        modelo.addAttribute("titulo", "Contrataciones registradas");

        return "contrataciones";
    }

    @GetMapping("/contrataciones/nueva")
    public String mostrarFormulario(Model modelo) {

        // Prepara los datos del formulario y las opciones disponibles
        modelo.addAttribute("contratacion", new ContratacionDTO());
        cargarOpciones(modelo);

        return "contratacion-formulario";
    }

    @PostMapping("/contrataciones")
    public String registrar(
            @ModelAttribute("contratacion") ContratacionDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        // Detecta errores al convertir los números o la fecha
        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisá la sucursal, el vigilante y la fecha.");
            cargarOpciones(modelo);
            return "contratacion-formulario";
        }

        // El identificador del registro nuevo lo genera la base de datos
        datos.setId(null);

        try {
            // Reutiliza las validaciones de referencias y duplicados
            servicio.registrar(datos);

            mensajes.addFlashAttribute(
                    "mensajeExito", "Contratación registrada correctamente.");

            // Evita repetir el registro al actualizar la página
            return "redirect:/contrataciones";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            cargarOpciones(modelo);
            return "contratacion-formulario";
        }
    }

    private void cargarOpciones(Model modelo) {

        // Carga las listas para seleccionar registros existentes
        modelo.addAttribute("sucursales", sucursalService.listar());
        modelo.addAttribute("vigilantes", vigilanteService.listar());
    }
    
    @GetMapping("/mis-contrataciones")
    public String listarPropias(
            Model modelo,
            Authentication autenticacion) {

        // Obtiene las contrataciones del titular de la cuenta autenticada
        modelo.addAttribute(
                "contrataciones",
                servicio.listarPropias(autenticacion.getName()));

        // La consulta propia no muestra opciones de administración
        modelo.addAttribute("esAdministrador", false);
        
        // Título de la consulta del vigilante autenticado
        modelo.addAttribute("titulo", "Mis contrataciones");

        // Reutiliza la tabla de contrataciones
        return "contrataciones";
    }
    
    @PostMapping("/contrataciones/{id}/eliminar")
    public String eliminar(
            @PathVariable int id,
            RedirectAttributes mensajes) {

        try {
            // Reutiliza la eliminación definida en el servicio
            servicio.eliminar(id);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Contratación eliminada correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        // Vuelve a la lista después de procesar la solicitud
        return "redirect:/contrataciones";
    }
    
        @GetMapping("/contrataciones/{id}/editar")
    public String mostrarEdicion(@PathVariable int id, Model modelo) {

        // Carga los datos actuales para mostrarlos en el formulario
        modelo.addAttribute("contratacion", servicio.consultarPorId(id));

        return "contratacion-edicion";
    }

    @PostMapping("/contrataciones/{id}/editar")
    public String actualizar(
            @PathVariable int id,
            @ModelAttribute("contratacion") ContratacionDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        // Conserva el identificador de la dirección al volver al formulario
        datos.setId(id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError", "Revisá la fecha ingresada.");
            return "contratacion-edicion";
        }

        try {
            servicio.actualizarDatos(
                    id, datos.getFecha(), datos.isConArma());

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Contratación actualizada correctamente.");

            return "redirect:/contrataciones";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "contratacion-edicion";
        }
    }
}