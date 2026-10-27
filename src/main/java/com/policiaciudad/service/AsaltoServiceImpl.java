package com.policiaciudad.service;

import com.policiaciudad.dto.AsaltoDTO;
import com.policiaciudad.repository.AsaltoRepository;
import com.policiaciudad.repository.PersonaDetenidaRepository;
import com.policiaciudad.repository.SucursalRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.modelo.Asalto;
import com.policiaciudad.repository.JuicioRepository;
import com.policiaciudad.exception.RecursoEnUsoException;

@Service // Spring registra el servicio
public class AsaltoServiceImpl implements AsaltoService {

    private final AsaltoRepository repositorio;
    private final SucursalRepository sucursalRepository;
    private final PersonaDetenidaRepository personaDetenidaRepository;
    private final JuicioRepository juicioRepository;

    public AsaltoServiceImpl(
            AsaltoRepository repositorio,
            SucursalRepository sucursalRepository,
            PersonaDetenidaRepository personaDetenidaRepository,
            JuicioRepository juicioRepository) {

        this.repositorio = repositorio;
        this.sucursalRepository = sucursalRepository;
        this.personaDetenidaRepository = personaDetenidaRepository;
        this.juicioRepository = juicioRepository;
    }

    @Override
    @Transactional // Ejecuta el registro dentro de una transacción
    public AsaltoDTO registrar(Integer codigoSucursal,
            Integer idPersonaDetenida, LocalDate fecha) {

        // Comprueba los identificadores antes de convertirlos a int
        if (codigoSucursal == null || idPersonaDetenida == null) {
            throw new DatosInvalidosException(
                    "La sucursal y la persona detenida son obligatorias."
            );
        }

        // El modelo valida identificadores positivos y fecha obligatoria
        Asalto asalto = new Asalto(
                codigoSucursal,
                idPersonaDetenida,
                fecha
        );

        if (!sucursalRepository.existsById(codigoSucursal)) {
            throw new RecursoNoEncontradoException(
                    "La sucursal no existe."
            );
        }

        if (!personaDetenidaRepository.existsById(idPersonaDetenida)) {
            throw new RecursoNoEncontradoException(
                    "La persona detenida no existe."
            );
        }

        Asalto guardado = repositorio.save(asalto);

        return new AsaltoDTO(
                guardado.getId(),
                guardado.getCodigoSucursal(),
                guardado.getIdPersonaDetenida(),
                guardado.getFecha()
        );
    }
    @Override
    @Transactional(readOnly = true) // Consulta sin modificar registros
    public List<AsaltoDTO> listar() {

        return repositorio.findAll()
                .stream()
                .map(asalto -> new AsaltoDTO(
                        asalto.getId(),
                        asalto.getCodigoSucursal(),
                        asalto.getIdPersonaDetenida(),
                        asalto.getFecha()
                )) // Convierte cada entidad en un DTO
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public AsaltoDTO consultarPorId(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del asalto debe ser positivo.");
        }

        // Busca el asalto o informa que no existe
        Asalto asalto = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un asalto con el identificador " + id + "."));

        return new AsaltoDTO(
                asalto.getId(),
                asalto.getCodigoSucursal(),
                asalto.getIdPersonaDetenida(),
                asalto.getFecha());
    }
    @Override
    @Transactional
    public AsaltoDTO actualizarFecha(int id, LocalDate fecha) {

        // Valida el identificador y la presencia de la fecha
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del asalto debe ser positivo.");
        }

        if (fecha == null) {
            throw new DatosInvalidosException(
                    "La fecha del asalto es obligatoria.");
        }

        // Busca el asalto que se quiere corregir
        Asalto asalto = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un asalto con el identificador " + id + "."));

        // Modifica únicamente la fecha
        asalto.corregirFecha(fecha);

        Asalto actualizado = repositorio.save(asalto);

        return new AsaltoDTO(
                actualizado.getId(),
                actualizado.getCodigoSucursal(),
                actualizado.getIdPersonaDetenida(),
                actualizado.getFecha());
    }

    @Override
    @Transactional
    public void eliminar(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del asalto debe ser positivo.");
        }

        Asalto asalto = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un asalto con el identificador " + id + "."));

        if (juicioRepository.existsByIdAsalto(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar el asalto porque tiene un juicio asociado.");
        }

        repositorio.delete(asalto);
    }
}