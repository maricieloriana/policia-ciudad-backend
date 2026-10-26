package com.policiaciudad.security;

import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Spring registra el componente de búsqueda de cuentas
public class UsuarioDetailsService implements UserDetailsService { //es la interfaz de Spring Security para buscar cuentas por nombre

    private final UsuarioRepository repositorio;

    public UsuarioDetailsService(UsuarioRepository repositorio) {
        this.repositorio = repositorio; // Inyección por constructor
    }

    @Override
    @Transactional(readOnly = true) // La operación solo consulta datos
    public UserDetails loadUserByUsername(String nombreUsuario) {
//contiene los datos que necesita la autenticación
        Usuario usuario = repositorio.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException( //si la búsqueda está vacía, lanza una excepción; () -> indica cómo crearla cuando se necesite
                        "Credenciales invalidas."
                ));

        return User.withUsername(usuario.getNombreUsuario()) //es una clase de Spring Security
                .password(usuario.getPasswordHash()) // Hash ya almacenado
                .roles(usuario.getRol().name()) // Nombre del rol del modelo convierte nuestro rol en una autoridad con prefijo
                .build();
    }
}
//Esta clase busca la cuenta. La comprobación de la contraseña corresponde al mecanismo de autenticación y al PasswordEncoder.