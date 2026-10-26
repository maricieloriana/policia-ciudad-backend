package com.policiaciudad.controller;

import com.policiaciudad.dto.ContratacionDTO;
import com.policiaciudad.service.ContratacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarArmaContratacionDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import com.policiaciudad.dto.ActualizarFechaContratacionDTO;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController // Activa el controlador para recibir solicitudes y devolver respuestas
//define la dirección -v
@RequestMapping("/api/contrataciones")
public class ContratacionController {

    private final ContratacionService servicio;

    public ContratacionController(ContratacionService servicio) {
        this.servicio = servicio;
    }

    @PostMapping //indica que el método atenderá solicitudes POST, destinadas a crear un registro
    public ResponseEntity<ContratacionDTO> registrar(
            @RequestBody ContratacionDTO datos) { //indica que los datos del cuerpo de la solicitud se cargarán en ContratacionDTO

        ContratacionDTO registrada = servicio.registrar(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrada);
    }
    @GetMapping // Atiende GET /api/contrataciones
    public List<ContratacionDTO> listar() {
        return servicio.listar(); // Delega la consulta al servicio
    }//El controlador delega las reglas al servicio y devuelve HTTP 201 con la contratación registrada
//Spring convertirá la lista de DTO en una respuesta JSON
    @GetMapping("/vigilantes") // Atiende GET /api/contrataciones/vigilantes
    public List<ContratacionDTO> listarPropias(Principal principal) {

        // Spring proporciona la identidad de la cuenta autenticada
        return servicio.listarPropias(principal.getName());
    }
    @GetMapping("/{id}")
    public ContratacionDTO consultarPorId(@PathVariable int id) {
        // Recibe el identificador desde la URL y delega la consulta
        return servicio.consultarPorId(id);
    }
    @PatchMapping("/{id}/arma")
    public ContratacionDTO actualizarCondicionArma(
            @PathVariable int id,
            @RequestBody ActualizarArmaContratacionDTO datos) {

        // Delega la actualización al servicio
        return servicio.actualizarCondicionArma(id, datos.getConArma());
    }
    @PatchMapping("/{id}/fecha")
    public ContratacionDTO actualizarFecha(
            @PathVariable int id,
            @RequestBody ActualizarFechaContratacionDTO datos) {

        // Delega la actualización de fecha al servicio
        return servicio.actualizarFecha(id, datos.getFecha());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {

        // Delega la eliminación al servicio
        servicio.eliminar(id);

        // Devuelve HTTP 204, sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
} 