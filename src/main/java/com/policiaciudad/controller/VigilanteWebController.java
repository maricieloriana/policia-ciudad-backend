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
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;

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
    public String registrar( //recibe los campos del formulario web
            @ModelAttribute("vigilante") RegistroVigilanteDTO datos,
            BindingResult resultado, //permite detectar errores de conversion
            Model modelo,
            RedirectAttributes mensajes) {

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá los datos ingresados y el formato de la fecha.");

            return "vigilante-formulario";
        }

        try {
            servicio.registrar(datos);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Vigilante registrado correctamente.");

            return "redirect:/vigilantes";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute(
                    "mensajeError",
                    excepcion.getMessage());

            return "vigilante-formulario";
        }
    }
    //Este método consulta al vigilante y carga sus datos en el DTO que utiliza el formulario
    @GetMapping("/vigilantes/{id}/editar")
    public String mostrarEdicion(@PathVariable int id, Model modelo) {

        VigilanteDTO actual = servicio.consultarPorId(id);

        RegistroVigilanteDTO datos = new RegistroVigilanteDTO();
        datos.setNombre(actual.getNombre());
        datos.setApellido(actual.getApellido());
        datos.setDni(actual.getDni());
        datos.setFechaNacimiento(actual.getFechaNacimiento());
        datos.setMail(actual.getMail());
        datos.setTelefono(actual.getTelefono());

        modelo.addAttribute("vigilante", datos);
        modelo.addAttribute("idVigilante", id);

        return "vigilante-formulario";
    }
    
    @PostMapping("/vigilantes/{id}/editar")
    public String actualizar(
            @PathVariable int id,
            @ModelAttribute("vigilante") RegistroVigilanteDTO datos,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes mensajes) {

        modelo.addAttribute("idVigilante", id);

        if (resultado.hasErrors()) {
            modelo.addAttribute(
                    "mensajeError",
                    "Revisá los datos ingresados y el formato de la fecha.");

            return "vigilante-formulario";
        }

        try {
            servicio.actualizarDatos(id, datos);

            mensajes.addFlashAttribute(
                    "mensajeExito",
                    "Vigilante actualizado correctamente.");

            return "redirect:/vigilantes";

        } catch (DatosInvalidosException
                | RegistroDuplicadoException excepcion) {

            modelo.addAttribute("mensajeError", excepcion.getMessage());

            return "vigilante-formulario";
        }
    }
    @PostMapping("/vigilantes/{id}/eliminar")
    public String eliminar(
            @PathVariable int id,
            RedirectAttributes mensajes) {

        try {
            // El servicio comprueba que no tenga contrataciones ni cuenta asociada
            servicio.eliminar(id);

            mensajes.addFlashAttribute(
                    "mensajeExito", "Vigilante eliminado correctamente.");

        } catch (DatosInvalidosException
                | RecursoNoEncontradoException
                | RecursoEnUsoException excepcion) {

            // Conserva el mensaje para mostrarlo después de volver a la lista
            mensajes.addFlashAttribute(
                    "mensajeError", excepcion.getMessage());
        }

        return "redirect:/vigilantes";
    }
    
    @GetMapping("/mi-perfil")
    public String mostrarPerfil(
            Model modelo,
            Authentication autenticacion) {

        // Consulta al vigilante vinculado con la cuenta autenticada
        modelo.addAttribute(
                "vigilante",
                servicio.consultarPropio(autenticacion.getName()));

        return "vigilante-perfil";
    }
}
