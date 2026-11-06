package com.policiaciudad.config;

import com.policiaciudad.modelo.Persona;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.PersonaRepository;
import com.policiaciudad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("carga-inicial")
public class AdministradorInicial implements CommandLineRunner {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String nombreUsuario;
    private final String contrasenia;
    private final String nombreUsuarioMetiche;
    private final String contraseniaMetiche;

    public AdministradorInicial(
            PersonaRepository personaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_INICIAL_USUARIO:}") String nombreUsuario,
            @Value("${ADMIN_INICIAL_PASSWORD:}") String contrasenia,
            @Value("${METICHE_INICIAL_USUARIO:metiche}") String nombreUsuarioMetiche,
            @Value("${METICHE_INICIAL_PASSWORD:}") String contraseniaMetiche) {

        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

        this.nombreUsuario = nombreUsuario.trim();
        this.contrasenia = contrasenia;

        this.nombreUsuarioMetiche = nombreUsuarioMetiche.trim();
        this.contraseniaMetiche = contraseniaMetiche;
    }

    @Override
    @Transactional
    public void run(String... args) {

        crearUsuarioInicial(
                nombreUsuario,
                contrasenia,
                Rol.ADMINISTRADOR,
                "Administrador inicial"
        );

        crearUsuarioInicial(
                nombreUsuarioMetiche,
                contraseniaMetiche,
                Rol.METICHE,
                "Usuario metiche"
        );
    }

    private void crearUsuarioInicial(
            String nombreUsuario,
            String contrasenia,
            Rol rol,
            String nombrePersona) {

        if (nombreUsuario.isBlank() || contrasenia.isBlank()) {
            throw new IllegalStateException(
                    "Debe informar el usuario y la contrasenia inicial."
            );
        }

        if (contrasenia.length() < 8) {
            throw new IllegalStateException("La contrasenia debe tener al menos 8 caracteres.");
        }

        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            System.out.println("La cuenta " + nombreUsuario + " ya existe; no se modifico.");
            return;
        }

        Persona titular = personaRepository.save(new Persona(nombrePersona));

        String hash = passwordEncoder.encode(contrasenia);

        Usuario usuario = new Usuario(titular.getId(),nombreUsuario,hash,rol);
        usuarioRepository.save(usuario);
        System.out.println("Cuenta inicial creada: " + nombreUsuario);
    }
}
