package com.policiaciudad.controller;

import com.policiaciudad.dto.EntidadBancariaDTO;
import com.policiaciudad.dto.RegistroEntidadBancariaDTO;
import com.policiaciudad.service.EntidadBancariaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarDomicilioCentralDTO;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/entidades-bancarias")
public class EntidadBancariaController {

    private final EntidadBancariaService servicio;

    public EntidadBancariaController(EntidadBancariaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<EntidadBancariaDTO> registrar(
            @RequestBody RegistroEntidadBancariaDTO datos) {

        // Delega el registro conjunto al servicio
        EntidadBancariaDTO registrada = servicio.registrar(datos);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrada);
    }
    @GetMapping
    public List<EntidadBancariaDTO> listar() {
        // Delega la consulta de entidades y sucursales al servicio
        return servicio.listar();
    }
    @GetMapping("/{codigo}")
    public EntidadBancariaDTO consultarPorCodigo(@PathVariable int codigo) {
        // Recibe el código desde la URL y delega la consulta
        return servicio.consultarPorCodigo(codigo);
    }
    @PatchMapping("/{codigo}/domicilio-central")
    public EntidadBancariaDTO actualizarDomicilioCentral(
            @PathVariable int codigo,
            @RequestBody ActualizarDomicilioCentralDTO datos) {

        // Delega la corrección del domicilio central al servicio
        return servicio.actualizarDomicilioCentral(
                codigo,
                datos.getDomicilioCentral());
    }
}