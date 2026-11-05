package com.policiaciudad.controller;

import com.policiaciudad.dto.VigilanteDTO;
import com.policiaciudad.service.VigilanteService;
import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.policiaciudad.dto.RegistroVigilanteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController // Recibe solicitudes y devuelve respuestas JSON
@RequestMapping("/api/vigilantes")
public class VigilanteController {

    private final VigilanteService servicio;

    public VigilanteController(VigilanteService servicio) {
        this.servicio = servicio; // Inyección mediante la interfaz
    }

    @GetMapping("/perfil") // Atiende GET /api/vigilantes/perfil
    public VigilanteDTO consultarPropio(Principal principal) {

        // Obtiene la identidad verificada por Spring Security
        return servicio.consultarPropio(principal.getName());
    }
 //el controlador obtiene la cuenta autenticada desde Principal y delega la consulta al servicio. 
//El cliente no envía un ID para seleccionar otra persona. La seguridad exige el rol VIGILANTE.
    @PostMapping // Atiende POST /api/vigilantes
    public ResponseEntity<VigilanteDTO> registrar(
            @RequestBody RegistroVigilanteDTO datos) { //carga los campos del JSON en DTO

        // Delega las validaciones y el guardado al servicio
        VigilanteDTO registrado = servicio.registrar(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrado);
    }
    @GetMapping
    public List<VigilanteDTO> listar() {
        // Delega la consulta general al servicio
        return servicio.listar();
    }
    @GetMapping("/{id}")
    public VigilanteDTO consultarPorId(@PathVariable int id) {
        // Recibe el identificador desde la URL y delega la consulta
        return servicio.consultarPorId(id);
    }
    
    @PatchMapping("/{id}/nombre")
    public VigilanteDTO actualizarDatos(
            @PathVariable int id,
            @RequestBody RegistroVigilanteDTO datos) {

        return servicio.actualizarDatos(id, datos);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {

        // Delega la eliminación y sus comprobaciones al servicio
        servicio.eliminar(id);

        // Devuelve HTTP 204, sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}