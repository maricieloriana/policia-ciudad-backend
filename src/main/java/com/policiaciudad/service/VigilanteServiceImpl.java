package com.policiaciudad.service;

import com.policiaciudad.dto.VigilanteDTO;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.modelo.Vigilante;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.repository.VigilanteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import java.util.List;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.ContratacionRepository;

@Service // Spring registra el servicio e inyecta sus dependencias
public class VigilanteServiceImpl implements VigilanteService { //Implementa el contrato

    private final UsuarioRepository usuarioRepository;
    private final VigilanteRepository vigilanteRepository;
    private final ContratacionRepository contratacionRepository;
    
// Recibe los repositorios mediante el constructor
    public VigilanteServiceImpl(
            UsuarioRepository usuarioRepository,
            VigilanteRepository vigilanteRepository,
            ContratacionRepository contratacionRepository) {

        this.usuarioRepository = usuarioRepository;
        this.vigilanteRepository = vigilanteRepository;
        this.contratacionRepository = contratacionRepository;
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
        return new VigilanteDTO(
                vigilante.getId(),
                vigilante.getNombreCompleto(),
                vigilante.getEdad()
        );
    }
//findById, heredado de JpaRepository, obtiene al vigilante mediante usuario.getIdPersona().
//Devuelve el identificador y el nombre heredados de Persona, junto con la edad propia de Vigilante
    @Override
    @Transactional // Guarda Persona y Vigilante en una misma transacción
    public VigilanteDTO registrar(String nombreCompleto, Integer edad) {

        // Valida la presencia del dato antes de convertirlo a int
        if (edad == null) {
            throw new DatosInvalidosException(
                    "La edad del vigilante es obligatoria."
            );
        }

        // El modelo valida el nombre y rechaza edades negativas
        Vigilante vigilante = new Vigilante(nombreCompleto, edad);

        // JPA guarda ambas tablas mediante JOINED
        Vigilante guardado = vigilanteRepository.save(vigilante);

        return new VigilanteDTO(
                guardado.getId(),
                guardado.getNombreCompleto(),
                guardado.getEdad()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<VigilanteDTO> listar() {

        // Convierte los vigilantes guardados en datos de respuesta
        return vigilanteRepository.findAll().stream()
                .map(vigilante -> new VigilanteDTO(
                        vigilante.getId(),
                        vigilante.getNombreCompleto(),
                        vigilante.getEdad()))
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

        return new VigilanteDTO(
                vigilante.getId(),
                vigilante.getNombreCompleto(),
                vigilante.getEdad());
    }
    @Override
    @Transactional
    public VigilanteDTO actualizarEdad(int id, Integer edad) {

        // Valida el identificador y la presencia de la edad
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del vigilante debe ser positivo.");
        }

        if (edad == null) {
            throw new DatosInvalidosException(
                    "La edad del vigilante es obligatoria.");
        }

        // Busca el vigilante que se quiere actualizar
        Vigilante vigilante = vigilanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vigilante con el identificador "
                        + id + "."));

        // El modelo rechaza edades negativas
        vigilante.actualizarEdad(edad);

        Vigilante actualizado = vigilanteRepository.save(vigilante);

        return new VigilanteDTO(
                actualizado.getId(),
                actualizado.getNombreCompleto(),
                actualizado.getEdad());
    }
    @Override
    @Transactional
    public VigilanteDTO actualizarNombre(int id, String nombreCompleto) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del vigilante debe ser positivo.");
        }

        // Comprueba el límite de la columna del nombre
        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres.");
        }

        // Busca el vigilante que se quiere actualizar
        Vigilante vigilante = vigilanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vigilante con el identificador "
                        + id + "."));

        // El método heredado de Persona valida y corrige el nombre
        vigilante.cambiarNombre(nombreCompleto);

        Vigilante actualizado = vigilanteRepository.save(vigilante);

        return new VigilanteDTO(
                actualizado.getId(),
                actualizado.getNombreCompleto(),
                actualizado.getEdad());
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
    public VigilanteDTO actualizarDatos(
            int id, String nombreCompleto, Integer edad) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del vigilante debe ser positivo.");
        }

        if (edad == null) {
            throw new DatosInvalidosException(
                    "La edad es obligatoria.");
        }

        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres.");
        }

        Vigilante vigilante = vigilanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vigilante con el identificador "
                        + id + "."));

        // El modelo valida ambos datos
        vigilante.cambiarNombre(nombreCompleto);
        vigilante.actualizarEdad(edad);

        Vigilante actualizado = vigilanteRepository.save(vigilante);

        return new VigilanteDTO(
                actualizado.getId(),
                actualizado.getNombreCompleto(),
                actualizado.getEdad());
    }
}