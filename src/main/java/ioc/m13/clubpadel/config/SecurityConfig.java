package ioc.m13.clubpadel.config;

import ioc.m13.clubpadel.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/*
 * Configuración de seguridad central del Backend para ClubPadel.
 * Define:
 *   - Qué rutas son públicas y cuáles requieren autenticación.
 *   - Qué roles (ADMIN/USER) pueden acceder a cada ruta.
 *   - Como se valida el JWT en cada petición.
 *   - Encoder de contraseñas.
 *   - Politicas CORS para permitir peticiones desde la app Android.
*/

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /* 
     * Filtro para manejar los tokens JWT 
    */
    @Autowired
    private JwtFilter jwtFilter;
    /**
     * Define cadena de filtros de seguridad: reglas de autorización, gestion de sesión, CORS, CSRF y filtro JWT.
     * @param http objeto de configuración de seguridad HTTP
     * @return cadena de filtros de construida
     * @throws Exception en caso de fallo al construir la cadena.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF desactivado para API stateless (no se usan cookies de sesión)
            .csrf(csrf -> csrf.disable())
            // Aplicar configuraciones CORS definidas en corsConfigurationSource()
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Sin sesiones HTTP, cada petición debe incluir el token JWT
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Reglas de autorización para cada ruta
            .authorizeHttpRequests(auth -> auth
                // Públicas
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pistas/**").permitAll()

                // Pistas: modificar solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/pistas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/pistas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/pistas/**").hasRole("ADMIN")

                // Reservas
                .requestMatchers(HttpMethod.POST, "/api/reservas/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/reservas/mis-reservas").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/reservas/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/reservas").hasRole("ADMIN")

                // Usuarios
                .requestMatchers(HttpMethod.GET, "/api/usuarios/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/password").authenticated()
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                // Verificación
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated() 
            )
            // Añade el filtro JWT antes del filtro de autenticación de Spring Security
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // Construye y devuelve la cadena de filtros    
        return http.build();
    }

    // Desactiva el registro automático del JwtFilter como servlet filter.
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
    // Bean encargado de codificar y verificar contraseñas usando BCrypt. Se inyecta en los servicios de usuario.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Obtiene el AuthenticationManager de Spring Security para autenticar usuarios en el servicio de autenticación.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    /*
     * Define la configuración CORS para permitir peticiones desde cualquier origen, 
     * con cualquier método y cabecera, y permitir credenciales (cookies, headers de autenticación).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}