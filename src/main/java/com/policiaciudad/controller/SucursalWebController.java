package com.policiaciudad.controller;

import com.policiaciudad.dto.RegistroSucursalDTO;
import com.policiaciudad.service.EntidadBancariaService;
import com.policiaciudad.service.SucursalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SucursalWebController {

    private final SucursalService servicio;
    private final EntidadBancariaService entidadService;

    public SucursalWebController(
            SucursalService servicio,
            EntidadBancariaService entidadService) {

        this.servicio = servicio;
        this.entidadService = entidadService;
    }

    @GetMapping("/sucursales")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("sucursales", servicio.listar());

        // Permite mostrar las acciones del administrador
        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "sucursales";
    }

    @GetMapping("/sucursales/nueva")
    public String mostrarFormulario(Model modelo) {

        // Prepara los datos y las entidades disponibles para seleccionar
        modelo.addAttribute("sucursal", new RegistroSucursalDTO());
        modelo.addAttribute("entidades", entidadService.listar());

        return "sucursal-formulario";
    }
    @PostMapping("/sucursales")
    public String registrar(
            @ModelAttribute("sucursal") RegistroSucursalDTO datos,
            BindingResult resultado,
            @RequestParam int codigoEntidadBancaria,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute(
                "codigoEntidadBancaria", codigoEntidadBancaria);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el código, el domicilio y la cantidad de empleados.");
            modelo.addAttribute("entidades", entidadService.listar());
            return "sucursal-formulario";
        }

        try {
            servicio.registrar(codigoEntidadBancaria, datos);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Sucursal registrada correctamente.");

            return "redirect:/sucursales";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            modelo.addAttribute("entidades", entidadService.listar());

            return "sucursal-formulario";
        }
    }
}