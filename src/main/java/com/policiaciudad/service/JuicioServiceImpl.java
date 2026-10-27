package com.policiaciudad.service;

import com.policiaciudad.dto.JuicioDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Asalto;
import com.policiaciudad.modelo.Juicio;
import com.policiaciudad.repository.AsaltoRepository;
import com.policiaciudad.repository.JuezRepository;
import com.policiaciudad.repository.JuicioRepository;
import com.policiaciudad.repository.PersonaDetenidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service // Spring registra el servicio
public class JuicioServiceImpl implements JuicioService {

    private final JuicioRepository repositorio;
    private final AsaltoRepository asaltoRepository;
    private final PersonaDetenidaRepository personaDetenidaRepository;
    private final JuezRepository juezRepository;

    public JuicioServiceImpl(
            JuicioRepository repositorio,
            AsaltoRepository asaltoRepository,
            PersonaDetenidaRepository personaDetenidaRepository,
            JuezRepository juezRepository) {

        this.repositorio = repositorio;
        this.asaltoRepository = asaltoRepository;
        this.personaDetenidaRepository = personaDetenidaRepository;
        this.juezRepository = juezRepository;
    }

    @Override
    @Transactional // Ejecuta las comprobaciones y el guardado en una transacción
    public JuicioDTO registrar(
            Integer idAsalto,
            Integer idPersonaDetenida,
            Integer idJuez,
            Boolean condenado,
            Integer aniosCarcel) {

        // Comprueba los datos antes de convertirlos a tipos primitivos
        if (idAsalto == null || idPersonaDetenida == null
                || idJuez == null || condenado == null
                || aniosCarcel == null) {

            throw new DatosInvalidosException(
                    "Todos los datos del juicio son obligatorios."
            );
        }

        // El modelo valida IDs positivos y coherencia de la resolución
        Juicio juicio = new Juicio(
                idAsalto,
                idPersonaDetenida,
                idJuez,
                condenado,
                aniosCarcel
        );

        Asalto asalto = asaltoRepository.findById(idAsalto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El asalto no existe."
                ));

        if (!personaDetenidaRepository.existsById(idPersonaDetenida)) {
            throw new RecursoNoEncontradoException(
                    "La persona detenida no existe."
            );
        }

        if (!juezRepository.existsById(idJuez)) {
            throw new RecursoNoEncontradoException(
                    "El juez no existe."
            );
        }

        // El detenido del juicio debe ser el mismo que el del asalto
        if (asalto.getIdPersonaDetenida()
                != juicio.getIdPersonaDetenida()) {

            throw new DatosInvalidosException(
                    "La persona detenida no coincide con la del asalto."
            );
        }

        // El UML permite como máximo un juicio por registro de asalto
        if (repositorio.existsByIdAsalto(idAsalto)) {
            throw new RegistroDuplicadoException(
                    "El asalto ya tiene un juicio registrado."
            );
        }

        Juicio guardado = repositorio.save(juicio);

        return new JuicioDTO(
                guardado.getId(),
                guardado.getIdAsalto(),
                guardado.getIdPersonaDetenida(),
                guardado.getIdJuez(),
                guardado.isCondenado(),
                guardado.getAniosCarcel()
        );
    }
    @Override
    @Transactional(readOnly = true) // Consulta sin modificar registros
    public List<JuicioDTO> listar() {

        return repositorio.findAll()
                .stream()
                .map(juicio -> new JuicioDTO(
                        juicio.getId(),
                        juicio.getIdAsalto(),
                        juicio.getIdPersonaDetenida(),
                        juicio.getIdJuez(),
                        juicio.isCondenado(),
                        juicio.getAniosCarcel()
                )) // Convierte cada entidad en un DTO
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public JuicioDTO consultarPorId(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juicio debe ser positivo.");
        }

        // Busca el juicio o informa que no existe
        Juicio juicio = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juicio con el identificador " + id + "."));

        return new JuicioDTO(
                juicio.getId(),
                juicio.getIdAsalto(),
                juicio.getIdPersonaDetenida(),
                juicio.getIdJuez(),
                juicio.isCondenado(),
                juicio.getAniosCarcel());
    }
    @Override
    @Transactional
    public JuicioDTO actualizarResolucion(
            int id, Boolean condenado, Integer aniosCarcel) {

        // Valida el identificador y los datos obligatorios
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juicio debe ser positivo.");
        }

        if (condenado == null || aniosCarcel == null) {
            throw new DatosInvalidosException(
                    "La condicion de condenado y los anios de carcel son obligatorios.");
        }

        // Busca el juicio que se quiere actualizar
        Juicio juicio = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juicio con el identificador " + id + "."));

        if (condenado) {

            // El modelo exige años positivos para una condena
            juicio.condenar(aniosCarcel);

        } else {

            // Una absolución debe tener cero años de cárcel
            if (aniosCarcel != 0) {
                throw new DatosInvalidosException(
                        "Una absolucion debe tener cero anios de carcel.");
            }

            juicio.absolver();
        }

        Juicio actualizado = repositorio.save(juicio);

        return new JuicioDTO(
                actualizado.getId(),
                actualizado.getIdAsalto(),
                actualizado.getIdPersonaDetenida(),
                actualizado.getIdJuez(),
                actualizado.isCondenado(),
                actualizado.getAniosCarcel());
    }
    
    @Override
    @Transactional
    public void eliminar(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juicio debe ser positivo.");
        }

        Juicio juicio = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juicio con el identificador " + id + "."));

        repositorio.delete(juicio);
    }
}