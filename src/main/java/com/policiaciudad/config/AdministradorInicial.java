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

@Component // Spring administra este componente
@Profile("carga-inicial") // Solo se activa con este perfil
public class AdministradorInicial implements CommandLineRunner { //permite ejecutar una tarea al arrancar la aplicación

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String nombreUsuario;
    private final String contrasenia;

    public AdministradorInicial(
            PersonaRepository personaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_INICIAL_USUARIO:}") String nombreUsuario,
            @Value("${ADMIN_INICIAL_PASSWORD:}") String contrasenia) {
            //value recibe las credenciales desde la configuración; en el siguiente paso las suministraremos mediante variables de entorno
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.nombreUsuario = nombreUsuario.trim();
        this.contrasenia = contrasenia;
    }

    @Override
    @Transactional // Guarda la persona y la cuenta en una misma transacción
    public void run(String... args) {

        if (nombreUsuario.isBlank() || contrasenia.isBlank()) {
            throw new IllegalStateException(
                    "Debe informar el usuario y la contrasenia inicial."
            );
        }

        if (contrasenia.length() < 8) {
            throw new IllegalStateException(
                    "La contrasenia inicial debe tener al menos 8 caracteres."
            );
        }

        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            System.out.println(
                    "La cuenta indicada ya existe; no se modifico."
            );
            return;
        }

        Persona titular = personaRepository.save(
                new Persona("Administrador inicial")
        );
            //encode genera el hash BCrypt antes de guardar la cuenta.
        String hash = passwordEncoder.encode(contrasenia);

        Usuario administrador = new Usuario(
                titular.getId(),
                nombreUsuario,
                hash,
                Rol.ADMINISTRADOR
        );

        usuarioRepository.save(administrador);

        System.out.println("Cuenta de administrador inicial creada.");
    }
}
