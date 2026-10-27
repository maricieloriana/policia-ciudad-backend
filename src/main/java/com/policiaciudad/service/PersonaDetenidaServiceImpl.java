package com.policiaciudad.service;

import com.policiaciudad.dto.PersonaDetenidaDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.modelo.PersonaDetenida;
import com.policiaciudad.repository.BandaRepository;
import com.policiaciudad.repository.PersonaDetenidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.AsaltoRepository;
import com.policiaciudad.repository.JuicioRepository;
import com.policiaciudad.repository.UsuarioRepository;

@Service // Spring registra el servicio
public class PersonaDetenidaServiceImpl
        implements PersonaDetenidaService {

    private final PersonaDetenidaRepository repositorio;
    private final BandaRepository bandaRepository;
    private final AsaltoRepository asaltoRepository;
    private final JuicioRepository juicioRepository;
    private final UsuarioRepository usuarioRepository;

    public PersonaDetenidaServiceImpl(
            PersonaDetenidaRepository repositorio,
            BandaRepository bandaRepository,
            AsaltoRepository asaltoRepository,
            JuicioRepository juicioRepository,
            UsuarioRepository usuarioRepository) {

        this.repositorio = repositorio;
        this.bandaRepository = bandaRepository;
        this.asaltoRepository = asaltoRepository;
        this.juicioRepository = juicioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional // Guarda Persona y PersonaDetenida juntas
    public PersonaDetenidaDTO registrar(
            String nombreCompleto, Integer numeroBanda) {

        // Comprueba el límite de la columna del nombre
        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {

            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres."
            );
        }

        // El constructor heredado valida el nombre
        PersonaDetenida detenida = new PersonaDetenida(nombreCompleto);

        // Solo comprueba la banda cuando se informa
        if (numeroBanda != null) {

            // El modelo valida que el número sea positivo
            detenida.asignarBanda(numeroBanda);

            if (!bandaRepository.existsById(numeroBanda)) {
                throw new RecursoNoEncontradoException(
                        "La banda indicada no existe."
                );
            }
        }

        PersonaDetenida guardada = repositorio.save(detenida);

        return new PersonaDetenidaDTO(
                guardada.getId(),
                guardada.getNombreCompleto(),
                guardada.getNumeroBanda()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<PersonaDetenidaDTO> listar() {

        // Convierte los registros guardados en datos de respuesta
        return repositorio.findAll().stream()
                .map(persona -> new PersonaDetenidaDTO(
                        persona.getId(),
                        persona.getNombreCompleto(),
                        persona.getNumeroBanda()))
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public PersonaDetenidaDTO consultarPorId(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la persona detenida debe ser positivo.");
        }

        // Busca la persona detenida o informa que no existe
        PersonaDetenida persona = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una persona detenida con el identificador "
                        + id + "."));

        return new PersonaDetenidaDTO(
                persona.getId(),
                persona.getNombreCompleto(),
                persona.getNumeroBanda());
    }
    @Override
    @Transactional
    public PersonaDetenidaDTO actualizarBanda(int id, Integer numeroBanda) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la persona detenida debe ser positivo.");
        }

        // Busca la persona detenida que se quiere actualizar
        PersonaDetenida persona = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una persona detenida con el identificador "
                        + id + "."));

        if (numeroBanda == null) {

            // Quita la asignación de banda
            persona.quitarBanda();

        } else {

            if (numeroBanda <= 0) {
                throw new DatosInvalidosException(
                        "El numero de la banda debe ser positivo.");
            }

            // Comprueba que la banda exista antes de asignarla
            if (!bandaRepository.existsById(numeroBanda)) {
                throw new RecursoNoEncontradoException(
                        "La banda indicada no existe.");
            }

            persona.asignarBanda(numeroBanda);
        }

        PersonaDetenida actualizada = repositorio.save(persona);

        return new PersonaDetenidaDTO(
                actualizada.getId(),
                actualizada.getNombreCompleto(),
                actualizada.getNumeroBanda());
    }
    @Override
    @Transactional
    public PersonaDetenidaDTO actualizarNombre(int id, String nombreCompleto) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la persona detenida debe ser positivo.");
        }

        // Comprueba el límite de la columna del nombre
        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres.");
        }

        // Busca la persona detenida que se quiere actualizar
        PersonaDetenida persona = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una persona detenida con el identificador "
                        + id + "."));

        // El método heredado de Persona valida y corrige el nombre
        persona.cambiarNombre(nombreCompleto);

        PersonaDetenida actualizada = repositorio.save(persona);

        return new PersonaDetenidaDTO(
                actualizada.getId(),
                actualizada.getNombreCompleto(),
                actualizada.getNumeroBanda());
    }
    @Override
    @Transactional
    public void eliminar(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la persona debe ser positivo.");
        }

        PersonaDetenida persona = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una persona detenida con el identificador "
                        + id + "."));

        if (asaltoRepository.existsByIdPersonaDetenida(id)
                || juicioRepository.existsByIdPersonaDetenida(id)) {

            throw new RecursoEnUsoException(
                    "No se puede eliminar la persona porque tiene asaltos o juicios asociados.");
        }

        if (usuarioRepository.existsByIdPersona(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar la persona porque tiene una cuenta vinculada.");
        }

        repositorio.delete(persona);
    }
}