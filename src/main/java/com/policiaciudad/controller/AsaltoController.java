package com.policiaciudad.controller;

import com.policiaciudad.dto.AsaltoDTO;
import com.policiaciudad.dto.RegistroAsaltoDTO;
import com.policiaciudad.service.AsaltoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarFechaAsaltoDTO;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController // Recibe solicitudes y devuelve respuestas JSON
@RequestMapping("/api/asaltos")
public class AsaltoController {

    private final AsaltoService servicio;

    public AsaltoController(AsaltoService servicio) {
        this.servicio = servicio; // Inyección mediante la interfaz
    }

    @PostMapping // Atiende POST /api/asaltos
    public ResponseEntity<AsaltoDTO> registrar(
            @RequestBody RegistroAsaltoDTO datos) {

        // El servicio valida las referencias y guarda el asalto
        AsaltoDTO registrado = servicio.registrar(
                datos.getCodigoSucursal(),
                datos.getIdPersonaDetenida(),
                datos.getFecha()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrado);
    }
    @GetMapping
    public List<AsaltoDTO> listar() {
        // Delega la consulta de asaltos al servicio
        return servicio.listar();
    }
    @GetMapping("/{id}")
    public AsaltoDTO consultarPorId(@PathVariable int id) {
        // Recibe el identificador desde la URL y delega la consulta
        return servicio.consultarPorId(id);
    }
    @PatchMapping("/{id}/fecha")
    public AsaltoDTO actualizarFecha(
            @PathVariable int id,
            @RequestBody ActualizarFechaAsaltoDTO datos) {

        // Delega la corrección de fecha al servicio
        return servicio.actualizarFecha(id, datos.getFecha());
    }
}
