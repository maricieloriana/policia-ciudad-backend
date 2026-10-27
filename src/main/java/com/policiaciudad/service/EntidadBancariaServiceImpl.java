package com.policiaciudad.service;

import com.policiaciudad.dto.EntidadBancariaDTO;
import com.policiaciudad.dto.RegistroEntidadBancariaDTO;
import com.policiaciudad.dto.RegistroSucursalDTO;
import com.policiaciudad.dto.SucursalDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.EntidadBancaria;
import com.policiaciudad.modelo.Sucursal;
import com.policiaciudad.repository.EntidadBancariaRepository;
import com.policiaciudad.repository.SucursalRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.AsaltoRepository;
import com.policiaciudad.repository.ContratacionRepository;

@Service
public class EntidadBancariaServiceImpl implements EntidadBancariaService {

    private final EntidadBancariaRepository entidadRepository;
    private final SucursalRepository sucursalRepository;
    private final AsaltoRepository asaltoRepository;
    private final ContratacionRepository contratacionRepository;

    public EntidadBancariaServiceImpl(
            EntidadBancariaRepository entidadRepository,
            SucursalRepository sucursalRepository,
            AsaltoRepository asaltoRepository,
            ContratacionRepository contratacionRepository) {

        this.entidadRepository = entidadRepository;
        this.sucursalRepository = sucursalRepository;
        this.asaltoRepository = asaltoRepository;
        this.contratacionRepository = contratacionRepository;
    }
    @Override
    @Transactional
    public EntidadBancariaDTO registrar(RegistroEntidadBancariaDTO datos) {

        // Comprueba los datos obligatorios
        if (datos == null || datos.getCodigo() == null) {
            throw new DatosInvalidosException(
                    "El codigo de la entidad es obligatorio.");
        }

        if (datos.getSucursales() == null
                || datos.getSucursales().size() < 2) {
            throw new DatosInvalidosException(
                    "La entidad debe tener al menos dos sucursales.");
        }

        validarDomicilio(datos.getDomicilioCentral());

        // El modelo valida el código y el domicilio central
        EntidadBancaria entidad = new EntidadBancaria(
                datos.getCodigo(),
                datos.getDomicilioCentral());

        if (entidadRepository.existsById(entidad.getCodigo())) {
            throw new RegistroDuplicadoException(
                    "Ya existe una entidad bancaria con ese codigo.");
        }

        List<Sucursal> sucursales = new ArrayList<>();
        Set<Integer> codigos = new HashSet<>(); //HashSet nos permite detectar códigos de sucursal repetidos en la solicitud.

        // Valida todas las sucursales antes de guardar
        for (RegistroSucursalDTO dato : datos.getSucursales()) {

            if (dato == null || dato.getCodigo() == null
                    || dato.getCantidadEmpleados() == null) {
                throw new DatosInvalidosException(
                        "Cada sucursal debe tener codigo y cantidad de empleados.");
            }

            validarDomicilio(dato.getDomicilio());

            Sucursal sucursal = new Sucursal(
                    dato.getCodigo(),
                    entidad.getCodigo(),
                    dato.getDomicilio(),
                    dato.getCantidadEmpleados());

            // Detecta códigos repetidos dentro de la misma solicitud
            if (!codigos.add(sucursal.getCodigo())) {
                throw new RegistroDuplicadoException(
                        "La solicitud contiene sucursales con el mismo codigo.");
            }

            if (sucursalRepository.existsById(sucursal.getCodigo())) {
                throw new RegistroDuplicadoException(
                        "Ya existe una sucursal con el codigo "
                        + sucursal.getCodigo() + ".");
            }

            sucursales.add(sucursal);
        }

        // Guarda primero la entidad que referencian las sucursales
        entidadRepository.saveAndFlush(entidad); //saveAndFlush envía el registro de la entidad a la base antes de guardar sus sucursales; la transacción sigue permitiendo deshacer todo si ocurre un error.

        List<SucursalDTO> respuestas = new ArrayList<>();

        for (Sucursal sucursal : sucursales) {
            Sucursal guardada = sucursalRepository.save(sucursal);

            respuestas.add(new SucursalDTO(
                    guardada.getCodigo(),
                    guardada.getCodigoEntidadBancaria(),
                    guardada.getDomicilio(),
                    guardada.getCantidadEmpleados()));
        }

        return new EntidadBancariaDTO(
                entidad.getCodigo(),
                entidad.getDomicilioCentral(),
                respuestas);
    }

    private void validarDomicilio(String domicilio) {

        if (domicilio == null || domicilio.isBlank()
                || domicilio.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El domicilio es obligatorio y admite hasta 255 caracteres.");
        }
    }
    @Override
    @Transactional(readOnly = true)
    public List<EntidadBancariaDTO> listar() {

        return entidadRepository.findAll().stream()
                .map(entidad -> {

                    // Busca las sucursales de esta entidad
                    List<SucursalDTO> sucursales = sucursalRepository
                            .findByCodigoEntidadBancaria(entidad.getCodigo())
                            .stream()
                            .map(sucursal -> new SucursalDTO(
                                    sucursal.getCodigo(),
                                    sucursal.getCodigoEntidadBancaria(),
                                    sucursal.getDomicilio(),
                                    sucursal.getCantidadEmpleados()))
                            .toList();

                    // Devuelve la entidad junto con sus sucursales
                    return new EntidadBancariaDTO(
                            entidad.getCodigo(),
                            entidad.getDomicilioCentral(),
                            sucursales);
                })
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public EntidadBancariaDTO consultarPorCodigo(int codigo) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la entidad debe ser positivo.");
        }

        // Busca la entidad o informa que no existe
        EntidadBancaria entidad = entidadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad bancaria con el codigo "
                        + codigo + "."));

        // Consulta las sucursales asociadas a esta entidad
        List<SucursalDTO> sucursales = sucursalRepository
                .findByCodigoEntidadBancaria(codigo)
                .stream()
                .map(sucursal -> new SucursalDTO(
                        sucursal.getCodigo(),
                        sucursal.getCodigoEntidadBancaria(),
                        sucursal.getDomicilio(),
                        sucursal.getCantidadEmpleados()))
                .toList();

        return new EntidadBancariaDTO(
                entidad.getCodigo(),
                entidad.getDomicilioCentral(),
                sucursales);
    }
    @Override
    @Transactional
    public EntidadBancariaDTO actualizarDomicilioCentral(
            int codigo, String domicilioCentral) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la entidad debe ser positivo.");
        }

        // Busca la entidad que se quiere actualizar
        EntidadBancaria entidad = entidadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad bancaria con el codigo "
                        + codigo + "."));

        // El modelo valida y corrige el domicilio central
        entidad.cambiarDomicilioCentral(domicilioCentral);

        EntidadBancaria actualizada = entidadRepository.save(entidad);

        // Prepara las sucursales que se incluyen en la respuesta
        List<SucursalDTO> sucursales = sucursalRepository
                .findByCodigoEntidadBancaria(codigo)
                .stream()
                .map(sucursal -> new SucursalDTO(
                        sucursal.getCodigo(),
                        sucursal.getCodigoEntidadBancaria(),
                        sucursal.getDomicilio(),
                        sucursal.getCantidadEmpleados()))
                .toList();

        return new EntidadBancariaDTO(
                actualizada.getCodigo(),
                actualizada.getDomicilioCentral(),
                sucursales);
    }

    @Override
    @Transactional
    public void eliminar(int codigo) {

        if (codigo <= 0) {
            throw new DatosInvalidosException(
                    "El codigo de la entidad debe ser positivo.");
        }

        EntidadBancaria entidad = entidadRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una entidad bancaria con el codigo "
                        + codigo + "."));

        List<Sucursal> sucursales
                = sucursalRepository.findByCodigoEntidadBancaria(codigo);

        // Comprueba todas las sucursales antes de eliminar cualquiera
        for (Sucursal sucursal : sucursales) {
            int codigoSucursal = sucursal.getCodigo();

            if (asaltoRepository.existsByCodigoSucursal(codigoSucursal)
                    || contratacionRepository.existsByCodigoSucursal(
                            codigoSucursal)) {

                throw new RecursoEnUsoException(
                        "No se puede eliminar la entidad: la sucursal "
                        + codigoSucursal
                        + " tiene asaltos o contrataciones asociados.");
            }
        }

        // Elimina primero las sucursales y después la entidad
        sucursalRepository.deleteAll(sucursales);
        sucursalRepository.flush();

        entidadRepository.delete(entidad);
    }
}