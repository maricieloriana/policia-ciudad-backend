package com.policiaciudad.controller;

import com.policiaciudad.dto.JuicioDTO;
import com.policiaciudad.dto.RegistroJuicioDTO;
import com.policiaciudad.service.JuicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarResolucionJuicioDTO;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController // Recibe solicitudes y devuelve respuestas JSON
@RequestMapping("/api/juicios")
public class JuicioController {

    private final JuicioService servicio;

    public JuicioController(JuicioService servicio) {
        this.servicio = servicio; // Inyección mediante la interfaz
    }

    @PostMapping // Atiende POST /api/juicios
    public ResponseEntity<JuicioDTO> registrar(
            @RequestBody RegistroJuicioDTO datos) {

        // El servicio valida las relaciones y guarda la resolución
        JuicioDTO registrado = servicio.registrar(
                datos.getIdAsalto(),
                datos.getIdPersonaDetenida(),
                datos.getIdJuez(),
                datos.getCondenado(),
                datos.getAniosCarcel()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrado);
    }
    @GetMapping // Atiende GET /api/juicios
    public List<JuicioDTO> listar() {
        return servicio.listar(); // Delega la consulta al servicio
    }
    @GetMapping("/{id}")
    public JuicioDTO consultarPorId(@PathVariable int id) {
        // Recibe el identificador desde la URL y delega la consulta
        return servicio.consultarPorId(id);
    }
    @PatchMapping("/{id}/resolucion")
    public JuicioDTO actualizarResolucion(
            @PathVariable int id,
            @RequestBody ActualizarResolucionJuicioDTO datos) {

        // Delega la actualización de la resolución al servicio
        return servicio.actualizarResolucion(
                id,
                datos.getCondenado(),
                datos.getAniosCarcel());
    }
}
