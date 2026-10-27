package com.policiaciudad.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Configuración de los componentes de seguridad
public class ConfiguracionSeguridad {

    @Bean // Procesa y comprueba las contraseñas mediante BCrypt
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Define los controles que se aplican a las solicitudes
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception { //SecurityFilterChain aplica los controles antes de llegar al controlador
// declara posibles errores al construir la configuración
        http
                .authorizeHttpRequests(permisos -> permisos
                        // Permite el procesamiento interno de errores
                        .dispatcherTypeMatchers(DispatcherType.ERROR)
                        .permitAll()//permite consultar el estado sin autenticarse.

                        // La comprobación de estado es pública
                        .requestMatchers(HttpMethod.GET, "/api/estado")
                        .permitAll()

                        // Prepararemos esta ruta para obtener el token CSRF
                        .requestMatchers(HttpMethod.GET, "/api/csrf")
                        .authenticated() //exige una cuenta autenticada.

                        // Solo un administrador puede registrar contrataciones
                        .requestMatchers(
                                HttpMethod.POST, "/api/contrataciones")
                        .hasRole("ADMINISTRADOR") //exige ese rol para registrar contrataciones.
                        
                    // Solo un administrador puede registrar cuentas
                        .requestMatchers(HttpMethod.POST, "/api/usuarios")
                        .hasRole("ADMINISTRADOR")
                        
                        // Administradores e investigadores pueden consultar todas las contrataciones
                        .requestMatchers(HttpMethod.GET, "/api/contrataciones")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        // El vigilante solo accede a sus propias contrataciones
                        .requestMatchers(HttpMethod.GET, "/api/contrataciones/vigilantes")
                        .hasRole("VIGILANTE")
                        
                        // El vigilante puede consultar sus propios datos personales
                        .requestMatchers(HttpMethod.GET, "/api/vigilantes/perfil")
                        .hasRole("VIGILANTE")
                        
                        // Solo un administrador puede registrar vigilantes
                        .requestMatchers(HttpMethod.POST, "/api/vigilantes")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo un administrador puede registrar jueces
                        .requestMatchers(HttpMethod.POST, "/api/jueces")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo un administrador puede registrar personas detenidas
                        .requestMatchers(HttpMethod.POST, "/api/personas-detenidas")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo un administrador puede registrar asaltos
                        .requestMatchers(HttpMethod.POST, "/api/asaltos")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo un administrador puede registrar juicios
                        .requestMatchers(HttpMethod.POST, "/api/juicios")
                        .hasRole("ADMINISTRADOR")
                        
                        // Administradores e investigadores pueden consultar los juicios
                        .requestMatchers(HttpMethod.GET, "/api/juicios")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/asaltos")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.POST, "/api/bandas")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/bandas")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/personas-detenidas")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/jueces")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/vigilantes")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.POST, "/api/entidades-bancarias")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/entidades-bancarias")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/sucursales")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/sucursales/{codigo}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/entidades-bancarias/{codigo}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/bandas/{numero}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/jueces/{id}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/personas-detenidas/{id}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/asaltos/{id}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/juicios/{id}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/contrataciones/{id}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.GET, "/api/vigilantes/{id}")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/bandas/{numero}")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/vigilantes/{id}/edad")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/jueces/{id}/anios-servicio")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/sucursales/{codigo}/empleados")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PUT, "/api/personas-detenidas/{id}/banda")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/contrataciones/{id}/arma")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/contrataciones/{id}/fecha")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/asaltos/{id}/fecha")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/juicios/{id}/resolucion")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/vigilantes/{id}/nombre")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/jueces/{id}/nombre")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/personas-detenidas/{id}/nombre")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/sucursales/{codigo}/domicilio")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.PATCH, "/api/entidades-bancarias/{codigo}/domicilio-central")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.POST,"/api/sucursales/entidad/{codigoEntidadBancaria}")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.DELETE, "/api/contrataciones/{id}")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.DELETE, "/api/bandas/{numero}")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.DELETE, "/api/sucursales/{codigo}")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.DELETE, "/api/jueces/{id}")
                        .hasRole("ADMINISTRADOR")
                        
                        .requestMatchers(HttpMethod.DELETE, "/api/vigilantes/{id}")
                        .hasRole("ADMINISTRADOR")
                        
                        // Permite abrir la pantalla de acceso y cargar sus estilos
                        .requestMatchers(HttpMethod.GET, "/login", "/css/**")
                        .permitAll()

                        // La pantalla de inicio requiere una cuenta autenticada
                        .requestMatchers(HttpMethod.GET, "/inicio")
                        .authenticated()
                        
                        // Administradores e investigadores pueden abrir esta pantalla
                        .requestMatchers(HttpMethod.GET, "/vigilantes")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        // Solo el administrador puede abrir el formulario de registro
                        .requestMatchers(HttpMethod.GET, "/vigilantes/nuevo")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede guardar desde el formulario
                        .requestMatchers(HttpMethod.POST, "/vigilantes")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede abrir la edición
                        .requestMatchers(HttpMethod.GET, "/vigilantes/{id}/editar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede guardar la edición
                        .requestMatchers(HttpMethod.POST, "/vigilantes/{id}/editar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede eliminar vigilantes desde la web
                        .requestMatchers(HttpMethod.POST, "/vigilantes/{id}/eliminar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Permite consultar la lista web a administradores e investigadores
                        .requestMatchers(HttpMethod.GET, "/contrataciones")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        // Solo el administrador puede abrir el formulario de registro
                        .requestMatchers(HttpMethod.GET, "/contrataciones/nueva")
                        .hasRole("ADMINISTRADOR")

                        // Solo el administrador puede registrar desde la web
                        .requestMatchers(HttpMethod.POST, "/contrataciones")
                        .hasRole("ADMINISTRADOR")
                        
                        // Permite al vigilante consultar sus propias contrataciones
                        .requestMatchers(HttpMethod.GET, "/mis-contrataciones")
                        .hasRole("VIGILANTE")
                        
                        // Permite al vigilante consultar su propio perfil
                        .requestMatchers(HttpMethod.GET, "/mi-perfil")
                        .hasRole("VIGILANTE")
                        
                        // Solo el administrador puede eliminar contrataciones desde la web
                        .requestMatchers(HttpMethod.POST, "/contrataciones/{id}/eliminar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede abrir y guardar la edición
                        .requestMatchers(HttpMethod.GET,"/contrataciones/{id}/editar")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST,"/contrataciones/{id}/editar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Administradores e investigadores pueden consultar sucursales
                        .requestMatchers(HttpMethod.GET, "/sucursales")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        // Solo el administrador puede abrir el registro de sucursales
                        .requestMatchers(HttpMethod.GET, "/sucursales/nueva")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede registrar sucursales desde la web
                        .requestMatchers(HttpMethod.POST, "/sucursales")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede abrir y guardar la edición
                        .requestMatchers(HttpMethod.GET, "/sucursales/{codigo}/editar")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/sucursales/{codigo}/editar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede eliminar sucursales desde la web
                        .requestMatchers(HttpMethod.POST, "/sucursales/{codigo}/eliminar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Administradores e investigadores pueden consultar entidades bancarias
                        .requestMatchers(HttpMethod.GET, "/entidades-bancarias")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        // Solo el administrador puede editar entidades bancarias
                        .requestMatchers(HttpMethod.GET, "/entidades-bancarias/{codigo}/editar")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/entidades-bancarias/{codigo}/editar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede abrir el registro de entidades
                        .requestMatchers(HttpMethod.GET, "/entidades-bancarias/nueva")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede registrar entidades bancarias
                        .requestMatchers(HttpMethod.POST, "/entidades-bancarias")
                        .hasRole("ADMINISTRADOR")
                        
                        // Administradores e investigadores pueden consultar bandas
                        .requestMatchers(HttpMethod.GET, "/bandas")
                        .hasAnyRole("ADMINISTRADOR", "INVESTIGADOR")
                        
                        // Solo el administrador puede abrir y guardar el registro de bandas
                        .requestMatchers(HttpMethod.GET, "/bandas/nueva")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/bandas")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede abrir y guardar la edición de bandas
                        .requestMatchers(HttpMethod.GET, "/bandas/{numero}/editar")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/bandas/{numero}/editar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Solo el administrador puede eliminar bandas desde la web
                        .requestMatchers(HttpMethod.POST, "/bandas/{numero}/eliminar")
                        .hasRole("ADMINISTRADOR")
                        
                        // Las demás rutas quedan bloqueadas por ahora
                        .anyRequest().denyAll() //bloquea las rutas que aún no habilitamos
                )
                // Recibe las credenciales mediante HTTP Basic
                .httpBasic(Customizer.withDefaults())
                
                // Permite ingresar desde nuestro formulario HTML
                .formLogin(formulario -> formulario
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/inicio", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                // Permite cerrar la sesión
                .logout(salida -> salida
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // Mantiene activa la protección CSRF
                .csrf(Customizer.withDefaults());  //CSRF agrega un token de protección a las solicitudes que modifican datos

        return http.build();
    }
}