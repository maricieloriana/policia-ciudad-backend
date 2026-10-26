package com.policiaciudad.controller;

import com.policiaciudad.dto.SucursalDTO;
import com.policiaciudad.service.SucursalService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.policiaciudad.dto.ActualizarEmpleadosSucursalDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.policiaciudad.dto.ActualizarDomicilioDTO;
import com.policiaciudad.dto.RegistroSucursalDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService servicio;

    public SucursalController(SucursalService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<SucursalDTO> listar() {
        // Delega la consulta general al servicio
        return servicio.listar();
    }
    @GetMapping("/{codigo}")
    public SucursalDTO consultarPorCodigo(@PathVariable int codigo) {
        // Recibe el código desde la URL y delega la consulta
        return servicio.consultarPorCodigo(codigo);
    }
    @PatchMapping("/{codigo}/empleados")
    public SucursalDTO actualizarCantidadEmpleados(
            @PathVariable int codigo,
            @RequestBody ActualizarEmpleadosSucursalDTO datos) {

        // Delega la actualización al servicio
        return servicio.actualizarCantidadEmpleados(
                codigo,
                datos.getCantidadEmpleados());
    }
    @PatchMapping("/{codigo}/domicilio")
    public SucursalDTO actualizarDomicilio(
            @PathVariable int codigo,
            @RequestBody ActualizarDomicilioDTO datos) {

        // Delega la corrección del domicilio al servicio
        return servicio.actualizarDomicilio(codigo, datos.getDomicilio());
    }
    @PostMapping("/entidad/{codigoEntidadBancaria}")
    public ResponseEntity<SucursalDTO> registrar(
            @PathVariable int codigoEntidadBancaria,
            @RequestBody RegistroSucursalDTO datos) {

        // Delega el registro de la sucursal al servicio
        SucursalDTO registrada = servicio.registrar(
                codigoEntidadBancaria,
                datos);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrada);
    }
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo) {

        // Delega la eliminación y sus comprobaciones al servicio
        servicio.eliminar(codigo);

        // Devuelve HTTP 204, sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}