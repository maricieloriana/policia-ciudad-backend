package com.policiaciudad.controller;

import com.policiaciudad.dto.ActualizarUsuarioDTO;
import com.policiaciudad.dto.UsuarioDTO;
import com.policiaciudad.exception.RecursoEnUsoException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import com.policiaciudad.dto.RegistroUsuarioDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioWebController {

    private final UsuarioService servicio;

    public UsuarioWebController(UsuarioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/usuarios/nuevo")
    public String mostrarFormulario(Model modelo) {

        modelo.addAttribute("usuario", new RegistroUsuarioDTO());
        modelo.addAttribute("roles", Rol.values());

        return "usuario-formulario";
    }

    @PostMapping("/usuarios")
    public String registrar(
            @ModelAttribute("usuario") RegistroUsuarioDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            datos.setContrasenia(null);
            modelo.addAttribute("roles", Rol.values());
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá el ID de persona y el rol. Volvé a ingresar la contraseña.");

            return "usuario-formulario";
        }

        try {
            servicio.registrar(
                    datos.getIdPersona(),
                    datos.getNombreUsuario(),
                    datos.getContrasenia(),
                    datos.getRol());

            mensajes.addFlashAttribute(
                    "mensajeExito", "Cuenta de usuario registrada correctamente.");

            return "redirect:/usuarios";

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RegistroDuplicadoException excepcion) {

            datos.setContrasenia(null);
            modelo.addAttribute("roles", Rol.values());
            modelo.addAttribute(
                    "mensajeError",
                    excepcion.getMessage()
                    + " Volvé a ingresar la contraseña.");

            return "usuario-formulario";
        }
    }

    @GetMapping("/usuarios")
    public String listar(Model modelo, Authentication autenticacion) {
        modelo.addAttribute("usuarios", servicio.listar());
        modelo.addAttribute("nombreUsuarioActual", autenticacion.getName());
        return "usuarios";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String mostrarEdicion(@PathVariable int id, Model modelo) {
        UsuarioDTO actual = servicio.consultarPorId(id);
        ActualizarUsuarioDTO datos = new ActualizarUsuarioDTO();
        datos.setNombreUsuario(actual.getNombreUsuario());
        datos.setRol(actual.getRol());
        modelo.addAttribute("usuario", datos);
        cargarEdicion(id, modelo);
        return "usuario-edicion";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String actualizar(@PathVariable int id,
            @ModelAttribute("usuario") ActualizarUsuarioDTO datos,
            BindingResult resultado, Model modelo,
            RedirectAttributes mensajes, Authentication autenticacion) {

        if (resultado.hasErrors()) {
            modelo.addAttribute("mensajeError", "Revisa el nombre y el rol seleccionados.");
            cargarEdicion(id, modelo);
            return "usuario-edicion";
        }
        try {
            servicio.actualizar(id, datos.getNombreUsuario(), datos.getRol(),
                    autenticacion.getName());
            mensajes.addFlashAttribute("mensajeExito", "Cuenta actualizada correctamente.");
            return "redirect:/usuarios";
        } catch (DatosInvalidosException | RecursoNoEncontradoException
                | RegistroDuplicadoException | RecursoEnUsoException excepcion) {
            modelo.addAttribute("mensajeError", excepcion.getMessage());
            cargarEdicion(id, modelo);
            return "usuario-edicion";
        }
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminar(@PathVariable int id,
            RedirectAttributes mensajes, Authentication autenticacion) {
        try {
            servicio.eliminar(id, autenticacion.getName());
            mensajes.addFlashAttribute("mensajeExito", "Cuenta eliminada correctamente. La persona titular se conserva.");
        } catch (DatosInvalidosException | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {
            mensajes.addFlashAttribute("mensajeError", excepcion.getMessage());
        }
        return "redirect:/usuarios";
    }

    private void cargarEdicion(int id, Model modelo) {
        UsuarioDTO actual = servicio.consultarPorId(id);
        modelo.addAttribute("idUsuario", id);
        modelo.addAttribute("idPersona", actual.getIdPersona());
        modelo.addAttribute("roles", Rol.values());
    }

}