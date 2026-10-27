package com.policiaciudad.controller;

import com.policiaciudad.service.EntidadBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.policiaciudad.dto.ActualizarDomicilioCentralDTO;
import com.policiaciudad.dto.EntidadBancariaDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import com.policiaciudad.dto.RegistroEntidadBancariaDTO;
import com.policiaciudad.dto.RegistroSucursalDTO;
import java.util.ArrayList;
import com.policiaciudad.exception.RegistroDuplicadoException;
import org.springframework.validation.BindingResult;

@Controller
public class EntidadBancariaWebController {

    private final EntidadBancariaService servicio;

    public EntidadBancariaWebController(
            EntidadBancariaService servicio) {

        this.servicio = servicio;
    }

    @GetMapping("/entidades-bancarias")
    public String listar(Model modelo, Authentication autenticacion) {

        modelo.addAttribute("entidades", servicio.listar());

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority()
                        .equals("ROLE_ADMINISTRADOR"));

        modelo.addAttribute("esAdministrador", esAdministrador);

        return "entidades-bancarias";
    }
    
        @GetMapping("/entidades-bancarias/{codigo}/editar")
    public String mostrarEdicion(
            @PathVariable int codigo, Model modelo) {

        EntidadBancariaDTO actual = servicio.consultarPorCodigo(codigo);

        ActualizarDomicilioCentralDTO datos =
                new ActualizarDomicilioCentralDTO();

        datos.setDomicilioCentral(actual.getDomicilioCentral());

        modelo.addAttribute("entidad", datos);
        modelo.addAttribute("codigoEntidad", codigo);

        return "entidad-bancaria-edicion";
    }

    @PostMapping("/entidades-bancarias/{codigo}/editar")
    public String actualizar(
            @PathVariable int codigo,
            @ModelAttribute("entidad") ActualizarDomicilioCentralDTO datos,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("codigoEntidad", codigo);

        try {
            servicio.actualizarDomicilioCentral(
                    codigo, datos.getDomicilioCentral());

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Entidad bancaria actualizada correctamente.");

            return "redirect:/entidades-bancarias";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "entidad-bancaria-edicion";
        }
    }
    
    @GetMapping("/entidades-bancarias/nueva")
    public String mostrarFormulario(Model modelo) {

        RegistroEntidadBancariaDTO datos =
                new RegistroEntidadBancariaDTO();

        // Prepara las dos sucursales iniciales de la entidad
        ArrayList<RegistroSucursalDTO> sucursales = new ArrayList<>();
        sucursales.add(new RegistroSucursalDTO());
        sucursales.add(new RegistroSucursalDTO());

        datos.setSucursales(sucursales);

        modelo.addAttribute("entidad", datos);

        return "entidad-bancaria-formulario";
    }
    
    @PostMapping("/entidades-bancarias")
    public String registrar(
            @ModelAttribute("entidad") RegistroEntidadBancariaDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        // Detecta errores al convertir los campos numéricos
        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá los códigos y las cantidades de empleados.");

            return "entidad-bancaria-formulario";
        }

        try {
            // El servicio registra la entidad y sus sucursales
            servicio.registrar(datos);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Entidad bancaria registrada correctamente.");

            return "redirect:/entidades-bancarias";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());

            return "entidad-bancaria-formulario";
        }
    }
}