package ioc.m13.clubpadel.config;

import ioc.m13.clubpadel.security.CustomUserDetailsService;
import ioc.m13.clubpadel.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF (no aplica a APIs REST con JWT)
            .csrf(csrf -> csrf.disable())

            // Configuración de CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Sin sesiones: cada petición se autentica con su token
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Reglas de autorización por ruta
            .authorizeHttpRequests(auth -> auth
                // Públicas
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pistas/**").permitAll()

                // Pistas: modificar solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/pistas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/pistas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/pistas/**").hasRole("ADMIN")

                // Reservas
                .requestMatchers(HttpMethod.POST, "/api/reservas/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/reservas/mis-reservas").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/reservas/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/reservas").hasRole("ADMIN")

                // Usuarios
                .requestMatchers(HttpMethod.GET, "/api/usuarios/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/password").authenticated()
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                // Verificación en recepción
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )

            // Añadir el filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Codificador de contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Proveedor de autenticación que usa nuestro UserDetailsService y el encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Necesario para autenticar manualmente en el AuthService
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // CORS: permitir peticiones desde cualquier origen (en producción, restringir)
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