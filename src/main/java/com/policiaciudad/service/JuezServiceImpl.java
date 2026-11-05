package com.policiaciudad.service;

import com.policiaciudad.dto.JuezDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Juez;
import com.policiaciudad.repository.JuezRepository;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import java.sql.SQLException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.JuicioRepository;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.repository.PersonaRepository;
import com.policiaciudad.dto.RegistroJuezDTO;

@Service // Spring registra el servicio
public class JuezServiceImpl implements JuezService {
//Implementa JuezService y recibe el repositorio por constructor.
    private final JuezRepository repositorio;
    private final JuicioRepository juicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;

    public JuezServiceImpl(
            JuezRepository repositorio,
            JuicioRepository juicioRepository,
            UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository) {

        this.repositorio = repositorio;
        this.juicioRepository = juicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JuezDTO> listar() {
        return repositorio.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public JuezDTO consultarPorId(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juez debe ser positivo.");
        }

        // Busca el juez o informa que no existe
        Juez juez = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juez con el identificador " + id + "."));

        return convertirADTO(juez);
    }
    @Override
    @Transactional
    public JuezDTO actualizarAniosServicio(int id, Integer aniosServicio) {

        // Valida el identificador y la presencia del dato
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juez debe ser positivo.");
        }

        if (aniosServicio == null) {
            throw new DatosInvalidosException(
                    "Los anios de servicio son obligatorios.");
        }

        // Busca el juez que se quiere actualizar
        Juez juez = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juez con el identificador " + id + "."));

        // El modelo rechaza cantidades negativas
        juez.actualizarAniosServicio(aniosServicio);

        Juez actualizado = guardar(juez);

        return convertirADTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juez debe ser positivo.");
        }

        // Busca el juez antes de eliminarlo
        Juez juez = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juez con el identificador " + id + "."));

        // Protege las referencias de los juicios registrados
        if (juicioRepository.existsByIdJuez(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar el juez porque tiene juicios asociados.");
        }

        // Protege la cuenta vinculada a la persona
        if (usuarioRepository.existsByIdPersona(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar el juez porque tiene una cuenta de usuario vinculada.");
        }

        repositorio.delete(juez);
    }
    


    @Override
    @Transactional
    public JuezDTO registrar(RegistroJuezDTO datos) {

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos del juez son obligatorios.");
        }

        if (datos.getClaveInterna() == null
                || datos.getAniosServicio() == null) {

            throw new DatosInvalidosException(
                    "La clave interna y los años de servicio son obligatorios.");
        }

        Juez juez = new Juez(
                datos.getNombre(),
                datos.getApellido(),
                datos.getDni(),
                datos.getFechaNacimiento(),
                datos.getMail(),
                datos.getTelefono(),
                datos.getClaveInterna(),
                datos.getAniosServicio());

        if (personaRepository.existsByDni(juez.getDni())) {
            throw new RegistroDuplicadoException(
                    "Ya existe una persona con ese DNI.");
        }

        if (repositorio.existsByClaveInterna(juez.getClaveInterna())) {
            throw new RegistroDuplicadoException(
                    "La clave interna ya pertenece a un juez.");
        }

        Juez guardado = guardar(juez);

        return convertirADTO(guardado);
    }

    private JuezDTO convertirADTO(Juez juez) {
        return new JuezDTO(
                juez.getId(),
                juez.getNombreCompleto(),
                juez.getNombre(),
                juez.getApellido(),
                juez.getDni(),
                juez.getFechaNacimiento(),
                juez.calcularEdad(),
                juez.getMail(),
                juez.getTelefono(),
                juez.getClaveInterna(),
                juez.getAniosServicio());
    }
    @Override
    @Transactional
    public JuezDTO actualizarDatos(int id, RegistroJuezDTO datos) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juez debe ser positivo.");
        }

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos del juez son obligatorios.");
        }

        if (datos.getAniosServicio() == null
                || datos.getAniosServicio() < 0) {
            throw new DatosInvalidosException(
                    "Los años de servicio son obligatorios y no pueden ser negativos.");
        }

        Juez juez = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juez con el identificador "
                        + id + "."));

        String dni = datos.getDni();

        if (dni != null
                && personaRepository.existsByDniAndIdNot(dni.trim(), id)) {
            throw new RegistroDuplicadoException(
                    "Ya existe otra persona con ese DNI.");
        }

        juez.actualizarDatosPersonales(
                datos.getNombre(),
                datos.getApellido(),
                dni,
                datos.getFechaNacimiento(),
                datos.getMail(),
                datos.getTelefono());

        juez.actualizarAniosServicio(datos.getAniosServicio());

        Juez actualizado = guardar(juez);

        return convertirADTO(actualizado);
    }
    private Juez guardar(Juez persona) {
        try {
            return repositorio.saveAndFlush(persona);
        } catch (DataIntegrityViolationException excepcion) {
            Throwable causa = excepcion;
            while (causa != null) {
                if (causa instanceof SQLException sql && sql.getErrorCode() == 1062) {
                    throw new RegistroDuplicadoException("Ya existe una persona con ese DNI o un juez con esa clave interna.");
                }
                causa = causa.getCause();
            }
            throw excepcion;
        }
    }
}
