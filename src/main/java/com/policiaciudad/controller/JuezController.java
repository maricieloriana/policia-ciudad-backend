package com.policiaciudad.controller;

import com.policiaciudad.dto.JuezDTO;
import com.policiaciudad.dto.RegistroJuezDTO;
import com.policiaciudad.service.JuezService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarAniosServicioJuezDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import com.policiaciudad.dto.ActualizarNombreDTO;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController // Recibe solicitudes y devuelve respuestas JSON
@RequestMapping("/api/jueces")
public class JuezController {

    private final JuezService servicio;

    public JuezController(JuezService servicio) {
        this.servicio = servicio; // Inyección mediante la interfaz
    }

    @PostMapping // Atiende POST /api/jueces
    public ResponseEntity<JuezDTO> registrar(
            @RequestBody RegistroJuezDTO datos) {

        // El servicio valida y guarda los datos
        JuezDTO registrado = servicio.registrar(
                datos.getNombreCompleto(),
                datos.getClaveInterna(),
                datos.getAniosServicio()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrado);
    }
    //el controlador recibe RegistroJuezDTO, delega las reglas al servicio y devuelve HTTP 201 con JuezDTO. Spring Security exige el rol administrador y un token CSRF válido.
    @GetMapping
    public List<JuezDTO> listar() {
        // Delega la consulta al servicio
        return servicio.listar();
    }
    @GetMapping("/{id}")
    public JuezDTO consultarPorId(@PathVariable int id) {
        // Recibe el identificador desde la URL y delega la consulta
        return servicio.consultarPorId(id);
    }
    @PatchMapping("/{id}/anios-servicio")
    public JuezDTO actualizarAniosServicio(
            @PathVariable int id,
            @RequestBody ActualizarAniosServicioJuezDTO datos) {

        // Delega la actualización al servicio
        return servicio.actualizarAniosServicio(
                id,
                datos.getAniosServicio());
    }
    @PatchMapping("/{id}/nombre")
    public JuezDTO actualizarNombre(
            @PathVariable int id,
            @RequestBody ActualizarNombreDTO datos) {

        // Delega la corrección del nombre al servicio
        return servicio.actualizarNombre(id, datos.getNombreCompleto());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {

        // Delega la eliminación y sus comprobaciones al servicio
        servicio.eliminar(id);

        // Devuelve HTTP 204, sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}
