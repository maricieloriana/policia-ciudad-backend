package com.policiaciudad.controller;

import org.springframework.security.core.Authentication;
import com.policiaciudad.dto.CambioPasswordDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.RegistroUsuarioDTO;
import com.policiaciudad.dto.UsuarioDTO;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Recibe solicitudes y devuelve respuestas JSON
@RequestMapping("/api/usuarios") // Dirección del registro de cuentas
public class UsuarioController {

    private final UsuarioService servicio;

    public UsuarioController(UsuarioService servicio) {
        this.servicio = servicio; // Inyección mediante la interfaz
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> registrar(
            @RequestBody RegistroUsuarioDTO datos) {

        // El servicio valida, procesa la contraseña y guarda la cuenta
        Usuario registrado = servicio.registrar(
                datos.getIdPersona(),
                datos.getNombreUsuario(),
                datos.getContrasenia(),
                datos.getRol()
        );

        // Prepara la respuesta sin contraseña ni hash
        UsuarioDTO respuesta = new UsuarioDTO(
                registrado.getId(),
                registrado.getIdPersona(),
                registrado.getNombreUsuario(),
                registrado.getRol()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable int id,
            @RequestBody CambioPasswordDTO datos,
            Authentication autenticacion) {

        servicio.cambiarPassword(
                id,
                datos.getNuevaPassword(),
                autenticacion.getName()
        );

        return ResponseEntity.noContent().build();
    }

}
//el controlador recibe RegistroUsuarioDTO, delega en UsuarioService y responde 
//HTTP 201 con UsuarioDTO. Spring Security comprueba el rol y el token CSRF antes de ejecutar el controlador.
