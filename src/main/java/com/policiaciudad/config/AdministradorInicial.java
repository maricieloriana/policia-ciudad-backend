package com.policiaciudad.config;

import com.policiaciudad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("carga-inicial")
public class AdministradorInicial implements CommandLineRunner {

    private final UsuarioService servicio;
    private final String nombreUsuario;
    private final String contrasenia;

    public AdministradorInicial(UsuarioService servicio,
            @Value("${ADMIN_INICIAL_USUARIO:}") String nombreUsuario,
            @Value("${ADMIN_INICIAL_PASSWORD:}") String contrasenia) {
        this.servicio = servicio;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    @Override
    public void run(String... args) {
        servicio.crearAdministradorInicial(nombreUsuario, contrasenia);
    }
}
