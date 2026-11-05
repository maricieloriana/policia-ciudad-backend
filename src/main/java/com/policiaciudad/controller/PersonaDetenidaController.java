package com.policiaciudad.controller;

import com.policiaciudad.dto.PersonaDetenidaDTO;
import com.policiaciudad.dto.RegistroPersonaDetenidaDTO;
import com.policiaciudad.service.PersonaDetenidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarBandaPersonaDetenidaDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController // Recibe solicitudes y devuelve respuestas JSON
@RequestMapping("/api/personas-detenidas")
public class PersonaDetenidaController {

    private final PersonaDetenidaService servicio;

    public PersonaDetenidaController(PersonaDetenidaService servicio) {
        this.servicio = servicio; // Inyección mediante la interfaz
    }

    @PostMapping // Atiende POST /api/personas-detenidas
    public ResponseEntity<PersonaDetenidaDTO> registrar(
            @RequestBody RegistroPersonaDetenidaDTO datos) {

        // Delega las validaciones y el guardado al servicio
      PersonaDetenidaDTO registrada = servicio.registrar(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrada);
    }
    @GetMapping
    public List<PersonaDetenidaDTO> listar() {
        // Delega la consulta al servicio
        return servicio.listar();
    }
    @GetMapping("/{id}")
    public PersonaDetenidaDTO consultarPorId(@PathVariable int id) {
        // Recibe el identificador desde la URL y delega la consulta
        return servicio.consultarPorId(id);
    }
    @PutMapping("/{id}/banda") //Usamos PUT para reemplazar la asignación de banda: un número asigna una banda y null la quita
    public PersonaDetenidaDTO actualizarBanda(
            @PathVariable int id,
            @RequestBody ActualizarBandaPersonaDetenidaDTO datos) {

        // Reemplaza la asignación de banda de esta persona
        return servicio.actualizarBanda(id, datos.getNumeroBanda());
    }
    @PatchMapping("/{id}/nombre")
    public PersonaDetenidaDTO actualizarDatos(
            @PathVariable int id,
            @RequestBody RegistroPersonaDetenidaDTO datos) {

        return servicio.actualizarDatos(id, datos);
    }
}
