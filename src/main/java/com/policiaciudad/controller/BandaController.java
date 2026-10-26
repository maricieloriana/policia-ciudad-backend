package com.policiaciudad.controller;

import com.policiaciudad.dto.BandaDTO;
import com.policiaciudad.dto.RegistroBandaDTO;
import com.policiaciudad.service.BandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarBandaDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/bandas")
public class BandaController {

    private final BandaService servicio;

    // Spring proporciona el servicio mediante el constructor
    public BandaController(BandaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<BandaDTO> registrar(
            @RequestBody RegistroBandaDTO datos) {

        // Delega las validaciones y el guardado al servicio
        BandaDTO registrada = servicio.registrar(
                datos.getNumero(),
                datos.getCantidadMiembros());

        // Devuelve HTTP 201 con los datos registrados
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrada);
    }
    @GetMapping
    public List<BandaDTO> listar() {
        // Delega la consulta al servicio
        return servicio.listar();
    }
    @GetMapping("/{numero}")
    public BandaDTO consultarPorNumero(@PathVariable int numero) {
        // Recibe el número desde la URL y delega la consulta
        return servicio.consultarPorNumero(numero);
    }
    @PatchMapping("/{numero}") //Usamos PATCH porque modificamos un dato de la banda
    public BandaDTO actualizarCantidadMiembros(
            @PathVariable int numero,
            @RequestBody ActualizarBandaDTO datos) {

        // Delega la actualización al servicio
        return servicio.actualizarCantidadMiembros(
                numero,
                datos.getCantidadMiembros());
    }
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> eliminar(@PathVariable int numero) {

        // Delega la eliminación al servicio
        servicio.eliminar(numero);

        // Devuelve HTTP 204, sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}
