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
import com.policiaciudad.dto.SucursalDTO;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.exception.RecursoEnUsoException;

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
    
        @GetMapping("/sucursales/{codigo}/editar")
    public String mostrarEdicion(
            @PathVariable int codigo, Model modelo) {

        SucursalDTO actual = servicio.consultarPorCodigo(codigo);

        // Carga los datos actuales en el objeto del formulario
        RegistroSucursalDTO datos = new RegistroSucursalDTO();
        datos.setCodigo(actual.getCodigo());
        datos.setDomicilio(actual.getDomicilio());
        datos.setCantidadEmpleados(actual.getCantidadEmpleados());

        modelo.addAttribute("sucursal", datos);
        modelo.addAttribute("codigoSucursal", codigo);

        return "sucursal-edicion";
    }

    @PostMapping("/sucursales/{codigo}/editar")
    public String actualizar(
            @PathVariable int codigo,
            @ModelAttribute("sucursal") RegistroSucursalDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        // Conserva el código de la dirección si hay errores
        modelo.addAttribute("codigoSucursal", codigo);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el domicilio y la cantidad de empleados.");
            return "sucursal-edicion";
        }

        try {
            servicio.actualizarDatos(
                    codigo,
                    datos.getDomicilio(),
                    datos.getCantidadEmpleados());

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Sucursal actualizada correctamente.");

            return "redirect:/sucursales";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "sucursal-edicion";
        }
    }
    
    @PostMapping("/sucursales/{codigo}/eliminar")
    public String eliminar(
            @PathVariable int codigo,
            RedirectAttributes mensajes) {

        try {
            servicio.eliminar(codigo);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Sucursal eliminada correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {

            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/sucursales";
    }
}