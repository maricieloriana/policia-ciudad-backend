package com.policiaciudad.service;

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
}//Recibe sus repositorios y el procesador de contraseñas por constructor.
//Valida los datos y los límites del nombre y de la contraseña.
//Comprueba que la persona exista y que no se repitan el titular ni el nombre de acceso.
//Si el rol es VIGILANTE, exige que el titular exista en vigilantes.
//Genera el hash y guarda la cuenta dentro de una transacción.