package ioc.m13.clubpadel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

          @Bean
          public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                    http
                              .csrf(csrf -> csrf.disable())
                              .authorizeHttpRequests(auth -> auth
                              .anyRequest().permitAll()  // Permitir todo acceso temporalmente a todas las rutas
                              );
                    return http.build();
          }
}