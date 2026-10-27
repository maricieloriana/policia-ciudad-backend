package com.policiaciudad.service;

import com.policiaciudad.dto.JuezDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Juez;
import com.policiaciudad.repository.JuezRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.JuicioRepository;
import com.policiaciudad.repository.UsuarioRepository;

@Service // Spring registra el servicio
public class JuezServiceImpl implements JuezService {
//Implementa JuezService y recibe el repositorio por constructor.
    private final JuezRepository repositorio;
    private final JuicioRepository juicioRepository;
    private final UsuarioRepository usuarioRepository;

    public JuezServiceImpl(
            JuezRepository repositorio,
            JuicioRepository juicioRepository,
            UsuarioRepository usuarioRepository) {

        this.repositorio = repositorio;
        this.juicioRepository = juicioRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    @Transactional // Guarda Persona y Juez dentro de una transacción
    public JuezDTO registrar(String nombreCompleto,
            Integer claveInterna, Integer aniosServicio) {

        // Comprueba que los datos numéricos estén informados
        if (claveInterna == null || aniosServicio == null) {
            throw new DatosInvalidosException(
                    "La clave interna y los anios de servicio son obligatorios."
            );
        }

        // Comprueba el límite de la columna nombre_completo
        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {

            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres."
            );
        }

        // El modelo valida el nombre, la clave positiva y la antigüedad
        Juez juez = new Juez(
                nombreCompleto,
                claveInterna,
                aniosServicio
        );

        // Comprueba si la clave interna ya está registrada
        if (repositorio.existsByClaveInterna(juez.getClaveInterna())) {
            throw new RegistroDuplicadoException(
                    "La clave interna ya pertenece a un juez."
            );
        }

        Juez guardado = repositorio.save(juez);

        return new JuezDTO(
                guardado.getId(),
                guardado.getNombreCompleto(),
                guardado.getClaveInterna(),
                guardado.getAniosServicio()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<JuezDTO> listar() {

        // Convierte los jueces guardados en datos de respuesta
        return repositorio.findAll().stream()
                .map(juez -> new JuezDTO(
                        juez.getId(),
                        juez.getNombreCompleto(),
                        juez.getClaveInterna(),
                        juez.getAniosServicio()))
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

        return new JuezDTO(
                juez.getId(),
                juez.getNombreCompleto(),
                juez.getClaveInterna(),
                juez.getAniosServicio());
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

        Juez actualizado = repositorio.save(juez);

        return new JuezDTO(
                actualizado.getId(),
                actualizado.getNombreCompleto(),
                actualizado.getClaveInterna(),
                actualizado.getAniosServicio());
    }
    @Override
    @Transactional
    public JuezDTO actualizarNombre(int id, String nombreCompleto) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juez debe ser positivo.");
        }

        // Comprueba el límite de la columna del nombre
        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres.");
        }

        // Busca el juez que se quiere actualizar
        Juez juez = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juez con el identificador " + id + "."));

        // El método heredado de Persona valida y corrige el nombre
        juez.cambiarNombre(nombreCompleto);

        Juez actualizado = repositorio.save(juez);

        return new JuezDTO(
                actualizado.getId(),
                actualizado.getNombreCompleto(),
                actualizado.getClaveInterna(),
                actualizado.getAniosServicio());
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
    public JuezDTO actualizarDatos(
            int id, String nombreCompleto, Integer aniosServicio) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador del juez debe ser positivo.");
        }

        if (aniosServicio == null) {
            throw new DatosInvalidosException(
                    "Los anios de servicio son obligatorios.");
        }

        if (nombreCompleto != null
                && nombreCompleto.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre completo no debe superar 255 caracteres.");
        }

        Juez juez = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un juez con el identificador " + id + "."));

        // El modelo valida el nombre y los años de servicio
        juez.cambiarNombre(nombreCompleto);
        juez.actualizarAniosServicio(aniosServicio);

        Juez actualizado = repositorio.save(juez);

        return new JuezDTO(
                actualizado.getId(),
                actualizado.getNombreCompleto(),
                actualizado.getClaveInterna(),
                actualizado.getAniosServicio());
    }
}