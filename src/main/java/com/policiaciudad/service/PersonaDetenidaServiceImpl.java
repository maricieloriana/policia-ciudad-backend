package com.policiaciudad.service;

import com.policiaciudad.dto.PersonaDetenidaDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.modelo.PersonaDetenida;
import com.policiaciudad.repository.BandaRepository;
import com.policiaciudad.repository.PersonaDetenidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import java.sql.SQLException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.AsaltoRepository;
import com.policiaciudad.repository.JuicioRepository;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.dto.RegistroPersonaDetenidaDTO;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.repository.PersonaRepository;

@Service // Spring registra el servicio
public class PersonaDetenidaServiceImpl
        implements PersonaDetenidaService {

    private final PersonaDetenidaRepository repositorio;
    private final BandaRepository bandaRepository;
    private final AsaltoRepository asaltoRepository;
    private final JuicioRepository juicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PersonaService personaService;

    public PersonaDetenidaServiceImpl(
            PersonaDetenidaRepository repositorio,
            BandaRepository bandaRepository,
            AsaltoRepository asaltoRepository,
            JuicioRepository juicioRepository,
            UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository,
            PersonaService personaService) {

        this.repositorio = repositorio;
        this.bandaRepository = bandaRepository;
        this.asaltoRepository = asaltoRepository;
        this.juicioRepository = juicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.personaService = personaService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaDetenidaDTO> listar() {
        return repositorio.findAll().stream()
                .map(this::convertirADTO)
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

        return convertirADTO(persona);
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
            persona.setNumeroBanda(null);

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

            persona.setNumeroBanda(numeroBanda);
        }

        PersonaDetenida actualizada = guardar(persona);

        return convertirADTO(actualizada);
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

    @Override
    @Transactional
    public PersonaDetenidaDTO registrar(RegistroPersonaDetenidaDTO datos) {

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos de la persona son obligatorios.");
        }

        PersonaDetenida persona = new PersonaDetenida();
        personaService.actualizarDatosPersonales(persona,
                datos.getNombre(),
                datos.getApellido(),
                datos.getDni(),
                datos.getFechaNacimiento(),
                datos.getMail(),
                datos.getTelefono());

        if (personaRepository.existsByDni(persona.getDni())) {
            throw new RegistroDuplicadoException(
                    "Ya existe una persona con ese DNI.");
        }

        Integer numeroBanda = datos.getNumeroBanda();

        if (numeroBanda != null) {
            validarNumeroBanda(numeroBanda);
            if (!bandaRepository.existsById(numeroBanda)) {
                throw new RecursoNoEncontradoException(
                        "La banda indicada no existe.");
            }
            persona.setNumeroBanda(numeroBanda);
        }

        PersonaDetenida guardada = guardar(persona);

        return convertirADTO(guardada);
    }

    @Override
    @Transactional
    public PersonaDetenidaDTO actualizarDatos(
            int id, RegistroPersonaDetenidaDTO datos) {

        if (id <= 0 || datos == null) {
            throw new DatosInvalidosException(
                    "El identificador y los datos de la persona son obligatorios.");
        }

        PersonaDetenida persona = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una persona detenida con el identificador "
                        + id + "."));

        String dni = datos.getDni();

        if (dni != null
                && personaRepository.existsByDniAndIdNot(dni.trim(), id)) {
            throw new RegistroDuplicadoException(
                    "Ya existe otra persona con ese DNI.");
        }

        personaService.actualizarDatosPersonales(persona,
                datos.getNombre(),
                datos.getApellido(),
                dni,
                datos.getFechaNacimiento(),
                datos.getMail(),
                datos.getTelefono());

        PersonaDetenida actualizada = guardar(persona);

        return convertirADTO(actualizada);
    }

    private PersonaDetenida guardar(PersonaDetenida persona) {
        try {
            return repositorio.saveAndFlush(persona);
        } catch (DataIntegrityViolationException excepcion) {
            Throwable causa = excepcion;
            while (causa != null) {
                if (causa instanceof SQLException sql && sql.getErrorCode() == 1062) {
                    throw new RegistroDuplicadoException("Ya existe una persona con ese DNI.");
                }
                causa = causa.getCause();
            }
            throw excepcion;
        }
    }

    private void validarNumeroBanda(Integer numeroBanda) {
        if (numeroBanda == null || numeroBanda <= 0) {
            throw new DatosInvalidosException("El numero de banda debe ser positivo.");
        }
    }

    private PersonaDetenidaDTO convertirADTO(PersonaDetenida persona) {
        return new PersonaDetenidaDTO(persona,
                personaService.calcularEdad(persona.getFechaNacimiento()));
    }
}
