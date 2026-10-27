package com.policiaciudad.service;

import com.policiaciudad.dto.SucursalDTO;
import com.policiaciudad.repository.SucursalRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.modelo.Sucursal;
import com.policiaciudad.dto.RegistroSucursalDTO;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.repository.EntidadBancariaRepository;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.ContratacionRepository;
import com.policiaciudad.repository.AsaltoRepository;

@Service
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final EntidadBancariaRepository entidadRepository;
    private final ContratacionRepository contratacionRepository;
    private final AsaltoRepository asaltoRepository;

    // Spring proporciona el repositorio mediante el constructor
    public SucursalServiceImpl(
            SucursalRepository sucursalRepository,
            EntidadBancariaRepository entidadRepository,
            ContratacionRepository contratacionRepository,
            AsaltoRepository asaltoRepository) {

        this.sucursalRepository = sucursalRepository;
        this.entidadRepository = entidadRepository;
        this.contratacionRepository = contratacionRepository;
        this.asaltoRepository = asaltoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalDTO> listar() {

        // Convierte las sucursales guardadas en datos de respuesta
        return sucursalRepository.findAll().stream()
                .map(sucursal -> new SucursalDTO(
                        sucursal.getCodigo(),
                        sucursal.getCodigoEntidadBancaria(),
                        sucursal.getDomicilio(),
                        sucursal.getCantidadEmpleados()))
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public SucursalDTO consultarPorCodigo(int codigo) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la sucursal debe ser positivo.");
        }

        // Busca la sucursal o informa que no existe
        Sucursal sucursal = sucursalRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una sucursal con el codigo " + codigo + "."));

        return new SucursalDTO(
                sucursal.getCodigo(),
                sucursal.getCodigoEntidadBancaria(),
                sucursal.getDomicilio(),
                sucursal.getCantidadEmpleados());
    }
    @Override
    @Transactional
    public SucursalDTO actualizarCantidadEmpleados(
            int codigo, Integer cantidadEmpleados) {

        // Valida el código y la presencia del dato
        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la sucursal debe ser positivo.");
        }

        if (cantidadEmpleados == null) {
            throw new DatosInvalidosException(
                    "La cantidad de empleados es obligatoria.");
        }

        // Busca la sucursal que se quiere actualizar
        Sucursal sucursal = sucursalRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una sucursal con el codigo "
                        + codigo + "."));

        // El modelo rechaza cantidades negativas
        sucursal.actualizarCantidadEmpleados(cantidadEmpleados);

        Sucursal actualizada = sucursalRepository.save(sucursal);

        return new SucursalDTO(
                actualizada.getCodigo(),
                actualizada.getCodigoEntidadBancaria(),
                actualizada.getDomicilio(),
                actualizada.getCantidadEmpleados());
    }
    @Override
    @Transactional
    public SucursalDTO actualizarDomicilio(int codigo, String domicilio) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la sucursal debe ser positivo.");
        }

        // Busca la sucursal que se quiere actualizar
        Sucursal sucursal = sucursalRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una sucursal con el codigo "
                        + codigo + "."));

        // El modelo valida y corrige el domicilio
        sucursal.cambiarDomicilio(domicilio);

        Sucursal actualizada = sucursalRepository.save(sucursal);

        return new SucursalDTO(
                actualizada.getCodigo(),
                actualizada.getCodigoEntidadBancaria(),
                actualizada.getDomicilio(),
                actualizada.getCantidadEmpleados());
    }
    @Override
    @Transactional
    public SucursalDTO registrar(
            int codigoEntidadBancaria, RegistroSucursalDTO datos) {

        // Valida el código de la entidad y los datos obligatorios
        if (codigoEntidadBancaria <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la entidad debe ser positivo.");
        }

        if (datos == null || datos.getCodigo() == null
                || datos.getCantidadEmpleados() == null) {
            throw new DatosInvalidosException(
                    "El codigo y la cantidad de empleados son obligatorios.");
        }

        if (datos.getDomicilio() != null
                && datos.getDomicilio().trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El domicilio no debe superar 255 caracteres.");
        }

        // El modelo valida los valores de la sucursal
        Sucursal sucursal = new Sucursal(
                datos.getCodigo(),
                codigoEntidadBancaria,
                datos.getDomicilio(),
                datos.getCantidadEmpleados());

        if (!entidadRepository.existsById(codigoEntidadBancaria)) {
            throw new RecursoNoEncontradoException(
                    "La entidad bancaria indicada no existe.");
        }

        if (sucursalRepository.existsById(sucursal.getCodigo())) {
            throw new RegistroDuplicadoException(
                    "Ya existe una sucursal con ese codigo.");
        }

        Sucursal guardada = sucursalRepository.save(sucursal);

        return new SucursalDTO(
                guardada.getCodigo(),
                guardada.getCodigoEntidadBancaria(),
                guardada.getDomicilio(),
                guardada.getCantidadEmpleados());
    }
    @Override
    @Transactional
    public void eliminar(int codigo) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la sucursal debe ser positivo.");
        }

        // Busca la sucursal antes de eliminarla
        Sucursal sucursal = sucursalRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una sucursal con el codigo "
                        + codigo + "."));

        // Protege las contrataciones y los asaltos registrados
        if (contratacionRepository.existsByCodigoSucursal(codigo)
                || asaltoRepository.existsByCodigoSucursal(codigo)) {

            throw new RecursoEnUsoException(
                    "No se puede eliminar la sucursal porque tiene contrataciones o asaltos asociados.");
        }

        // Conserva el mínimo de dos sucursales de la entidad
        long cantidad = sucursalRepository.countByCodigoEntidadBancaria(
                sucursal.getCodigoEntidadBancaria());

        if (cantidad <= 2) {
            throw new RecursoEnUsoException(
                    "La entidad bancaria debe conservar al menos dos sucursales.");
        }

        sucursalRepository.delete(sucursal);
    }
    @Override
    @Transactional
    public SucursalDTO actualizarDatos(
            int codigo, String domicilio, Integer cantidadEmpleados) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la sucursal debe ser positivo.");
        }

        if (cantidadEmpleados == null) {
            throw new DatosInvalidosException(
                    "La cantidad de empleados es obligatoria.");
        }

        Sucursal sucursal = sucursalRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una sucursal con el codigo "
                        + codigo + "."));

        // Reutiliza las validaciones del modelo
        sucursal.cambiarDomicilio(domicilio);
        sucursal.actualizarCantidadEmpleados(cantidadEmpleados);

        Sucursal actualizada = sucursalRepository.save(sucursal);

        return new SucursalDTO(
                actualizada.getCodigo(),
                actualizada.getCodigoEntidadBancaria(),
                actualizada.getDomicilio(),
                actualizada.getCantidadEmpleados());
    }
}
