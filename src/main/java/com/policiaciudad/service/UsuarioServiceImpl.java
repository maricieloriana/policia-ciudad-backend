package com.policiaciudad.service;

import java.util.List;
import java.util.Comparator;
import com.policiaciudad.dto.UsuarioDTO;
import com.policiaciudad.exception.RecursoEnUsoException;
import org.springframework.security.access.AccessDeniedException;

import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.PersonaRepository;
import com.policiaciudad.repository.UsuarioRepository;
import com.policiaciudad.repository.VigilanteRepository;
import java.nio.charset.StandardCharsets;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Spring registra el servicio
public class UsuarioServiceImpl implements UsuarioService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VigilanteRepository vigilanteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            PersonaRepository personaRepository,
            UsuarioRepository usuarioRepository,
            VigilanteRepository vigilanteRepository,
            PasswordEncoder passwordEncoder) {

        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.vigilanteRepository = vigilanteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // Agrupa las operaciones del registro
    public Usuario registrar(int idPersona, String nombreUsuario,
            String contrasenia, Rol rol) {

        if (idPersona <= 0) {
            throw new DatosInvalidosException(
                    "El ID de la persona debe ser positivo."
            );
        }

        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new DatosInvalidosException(
                    "El nombre de usuario es obligatorio."
            );
        }

        if (contrasenia == null || contrasenia.isBlank()
                || contrasenia.length() < 8) {

            throw new DatosInvalidosException(
                    "La contrasenia debe tener al menos 8 caracteres."
            );
        }

        // BCrypt admite contraseñas de hasta 72 bytes
        if (contrasenia.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new DatosInvalidosException(
                    "La contrasenia no debe superar 72 bytes en UTF-8."
            );
        }

        if (rol == null) {
            throw new DatosInvalidosException(
                    "El rol es obligatorio."
            );
        }

        String nombre = nombreUsuario.trim();

        if (nombre.length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre de usuario no debe superar 255 caracteres."
            );
        }

        if (!personaRepository.existsById(idPersona)) {
            throw new RecursoNoEncontradoException(
                    "La persona no existe."
            );
        }

        if (usuarioRepository.existsByIdPersona(idPersona)) {
            throw new RegistroDuplicadoException(
                    "La persona ya tiene una cuenta."
            );
        }

        if (usuarioRepository.existsByNombreUsuario(nombre)) {
            throw new RegistroDuplicadoException(
                    "El nombre de usuario ya esta registrado."
            );
        }

        // El rol VIGILANTE solo puede asignarse a un Vigilante
        if (rol == Rol.VIGILANTE
                && !vigilanteRepository.existsById(idPersona)) {

            throw new DatosInvalidosException(
                    "El rol VIGILANTE requiere una persona vigilante."
            );
        }

        // Procesa la contraseña antes de construir la cuenta
        String hash = passwordEncoder.encode(contrasenia);

        Usuario usuario = new Usuario(
                idPersona,
                nombre,
                hash,
                rol
        );

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream()
                .sorted(Comparator.comparing(Usuario::getId))
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO consultarPorId(int id) {
        return convertirDTO(buscarUsuario(id));
    }

    @Override
    @Transactional
    public UsuarioDTO actualizar(int id, String nombreUsuario, Rol rol,
            String nombreAdministrador) {

        List<Usuario> administradores
                = usuarioRepository.bloquearPorRol(Rol.ADMINISTRADOR);
        Usuario usuario = buscarUsuario(id);
        validarOperacion(usuario, rol, nombreAdministrador, administradores);

        if (nombreUsuario == null || nombreUsuario.isBlank()
                || nombreUsuario.trim().length() > 255) {
            throw new DatosInvalidosException(
                    "El nombre de usuario es obligatorio y admite hasta 255 caracteres.");
        }
        if (rol == null) {
            throw new DatosInvalidosException("El rol es obligatorio.");
        }
        String nombre = nombreUsuario.trim();
        if (usuarioRepository.existsByNombreUsuarioAndIdNot(nombre, id)) {
            throw new RegistroDuplicadoException(
                    "El nombre de usuario ya esta registrado.");
        }
        if (rol == Rol.VIGILANTE
                && !vigilanteRepository.existsById(usuario.getIdPersona())) {
            throw new DatosInvalidosException(
                    "El rol VIGILANTE requiere una persona vigilante.");
        }
        usuario.cambiarNombreUsuario(nombre);
        usuario.cambiarRol(rol);
        return convertirDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminar(int id, String nombreAdministrador) {
        List<Usuario> administradores
                = usuarioRepository.bloquearPorRol(Rol.ADMINISTRADOR);
        Usuario usuario = buscarUsuario(id);
        validarOperacion(usuario, null, nombreAdministrador, administradores);
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public void cambiarPassword(int id, String nuevaPassword, String nombreUsuarioSolicitante) {
        Usuario solicitante = usuarioRepository
                .findByNombreUsuario(nombreUsuarioSolicitante)
                .orElseThrow(() -> new AccessDeniedException(
                "Usuario solicitante no encontrado."
        ));

        if (solicitante.getRol() != Rol.METICHE) {
            throw new AccessDeniedException(
                    "No tiene permisos para cambiar contrasenias."
            );
        }
        if (nuevaPassword == null || nuevaPassword.isBlank()
                || nuevaPassword.length() < 8) {

            throw new DatosInvalidosException(
                    "La contrasenia debe tener al menos 8 caracteres."
            );
        }

        if (nuevaPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new DatosInvalidosException("La contrasenia no debe superar 72 bytes en UTF-8.");
        }
        Usuario usuario = buscarUsuario(id);
        if (usuario.getNombreUsuario().equals(nombreUsuarioSolicitante)) {
            throw new AccessDeniedException("No puede cambiar su propia contrasenia.");
        }

        String hash = passwordEncoder.encode(nuevaPassword);
        usuario.cambiarPasswordHash(hash);
        usuarioRepository.save(usuario);
    }

    private Usuario buscarUsuario(int id) {
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la cuenta debe ser positivo.");
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe una cuenta con el identificador " + id + "."));
    }

    private void validarOperacion(Usuario cuenta, Rol rolDestino,
            String nombreAdministrador, List<Usuario> administradores) {

        Usuario administrador = usuarioRepository
                .findByNombreUsuario(nombreAdministrador)
                .orElseThrow(() -> new AccessDeniedException(
                "La cuenta administradora no esta disponible."));
        if (administrador.getRol() != Rol.ADMINISTRADOR) {
            throw new AccessDeniedException("La operacion requiere un administrador.");
        }
        if (cuenta.getRol() == Rol.ADMINISTRADOR
                && rolDestino != Rol.ADMINISTRADOR
                && administradores.size() <= 1) {
            throw new RecursoEnUsoException(
                    "El sistema debe conservar al menos una cuenta administradora.");
        }
        if (cuenta.getId().equals(administrador.getId())) {
            throw new RecursoEnUsoException(
                    "No podes modificar ni eliminar la cuenta que estas usando. Ingresa con otra cuenta administradora.");
        }
    }

    private UsuarioDTO convertirDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getIdPersona(),
                usuario.getNombreUsuario(), usuario.getRol());
    }

}//Recibe sus repositorios y el procesador de contraseñas por constructor.
//Valida los datos y los límites del nombre y de la contraseña.
//Comprueba que la persona exista y que no se repitan el titular ni el nombre de acceso.
//Si el rol es VIGILANTE, exige que el titular exista en vigilantes.
//Genera el hash y guarda la cuenta dentro de una transacción.
