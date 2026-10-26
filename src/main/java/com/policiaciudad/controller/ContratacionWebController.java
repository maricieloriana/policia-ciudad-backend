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
}