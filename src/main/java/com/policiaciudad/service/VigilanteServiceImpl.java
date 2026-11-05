package com.policiaciudad.service;

import com.policiaciudad.dto.VigilanteDTO;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.modelo.Vigilante;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.repository.VigilanteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import java.sql.SQLException;
import org.springframework.transaction.annotation.Transactional;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import java.util.List;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.ContratacionRepository;
import com.policiaciudad.repository.PersonaRepository;
import com.policiaciudad.dto.RegistroVigilanteDTO;
import com.policiaciudad.exception.RegistroDuplicadoException;

@Service // Spring registra el servicio e inyecta sus dependencias
public class VigilanteServiceImpl implements VigilanteService { //Implementa el contrato

    private final UsuarioRepository usuarioRepository;
    private final VigilanteRepository vigilanteRepository;
    private final ContratacionRepository contratacionRepository;
    private final PersonaRepository personaRepository;
    
// Recibe los repositorios mediante el constructor
    public VigilanteServiceImpl(
            UsuarioRepository usuarioRepository,
            VigilanteRepository vigilanteRepository,
            ContratacionRepository contratacionRepository,
            PersonaRepository personaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.vigilanteRepository = vigilanteRepository;
        this.contratacionRepository = contratacionRepository;
        this.personaRepository = personaRepository;
    }
//Comprueba la cuenta y el rol.
    @Override
    @Transactional(readOnly = true) // La operación solo consulta datos
    public VigilanteDTO consultarPropio(String nombreUsuario) {

        // Busca la cuenta indicada por la autenticación
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new AccessDeniedException(
                        "La cuenta no esta disponible."
                ));

        // La consulta propia requiere una cuenta de vigilante
        if (usuario.getRol() != Rol.VIGILANTE) {
            throw new AccessDeniedException(
                    "La consulta propia requiere el rol VIGILANTE."
            );
        }

        // Busca al titular usando idPersona, no el ID de la cuenta
        Vigilante vigilante = vigilanteRepository
                .findById(usuario.getIdPersona())
                .orElseThrow(() -> new AccessDeniedException(
                        "La cuenta no esta asociada a un vigilante."
                ));

        // Devuelve únicamente los datos personales definidos en el DTO
        return convertirADTO(vigilante);
    }
//findById, heredado de JpaRepository, obtiene al vigilante mediante usuario.getIdPersona().
//Devuelve el identificador y el nombre heredados de Persona, junto con la edad propia de Vigilante


    @Override
    @Transactional(readOnly = true)
    public List<VigilanteDTO> listar() {
        return vigilanteRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public VigilanteDTO consultarPorId(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del vigilante debe ser positivo.");
        }

        // Busca el vigilante o informa que no existe
        Vigilante vigilante = vigilanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vigilante con el identificador "
                        + id + "."));

     return convertirADTO(vigilante);
    }


    @Override
    @Transactional
    public void eliminar(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del vigilante debe ser positivo.");
        }

        // Busca el vigilante antes de eliminarlo
        Vigilante vigilante = vigilanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vigilante con el identificador "
                        + id + "."));

        // Protege las contrataciones registradas
        if (contratacionRepository.existsByIdVigilante(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar el vigilante porque tiene contrataciones asociadas.");
        }

        // Protege la cuenta vinculada a la persona
        if (usuarioRepository.existsByIdPersona(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar el vigilante porque tiene una cuenta de usuario vinculada.");
        }

        vigilanteRepository.delete(vigilante);
    }

    @Override
    @Transactional
    public VigilanteDTO registrar(RegistroVigilanteDTO datos) {

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos del vigilante son obligatorios.");
        }

        Vigilante vigilante = new Vigilante(
                datos.getNombre(),
                datos.getApellido(),
                datos.getDni(),
                datos.getFechaNacimiento(),
                datos.getMail(),
                datos.getTelefono());

        if (personaRepository.existsByDni(vigilante.getDni())) {
            throw new RegistroDuplicadoException(
                    "Ya existe una persona con ese DNI.");
        }

        Vigilante guardado = guardar(vigilante);

        return convertirADTO(guardado);
    }
    //Este método concentra la conversión para reutilizarla en las distintas operaciones
    private VigilanteDTO convertirADTO(Vigilante vigilante) {
        return new VigilanteDTO(
                vigilante.getId(),
                vigilante.getNombreCompleto(),
                vigilante.getNombre(),
                vigilante.getApellido(),
                vigilante.getDni(),
                vigilante.getFechaNacimiento(),
                vigilante.calcularEdad(),
                vigilante.getMail(),
                vigilante.getTelefono());
    }
    @Override
    @Transactional
    public VigilanteDTO actualizarDatos(
            int id, RegistroVigilanteDTO datos) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del vigilante debe ser positivo.");
        }

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos del vigilante son obligatorios.");
        }

        Vigilante vigilante = vigilanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vigilante con el identificador "
                        + id + "."));

        String dni = datos.getDni();

        if (dni != null
                && personaRepository.existsByDniAndIdNot(dni.trim(), id)) {

            throw new RegistroDuplicadoException(
                    "Ya existe otra persona con ese DNI.");
        }

        vigilante.actualizarDatosPersonales(
                datos.getNombre(),
                datos.getApellido(),
                dni,
                datos.getFechaNacimiento(),
                datos.getMail(),
                datos.getTelefono());

        Vigilante actualizado = guardar(vigilante);

        return convertirADTO(actualizado);
    }
    private Vigilante guardar(Vigilante persona) {
        try {
            return vigilanteRepository.saveAndFlush(persona);
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
}
