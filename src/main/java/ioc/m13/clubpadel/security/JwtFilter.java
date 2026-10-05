package ioc.m13.clubpadel.security;

import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component 
public class JwtFilter extends OncePerRequestFilter {
          // lógica para filtrar y validar el JWT en cada petición

          @Autowired
          private JwtUtil jwtUtil;

          @Override
          protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

                    // 1. Lee la cabecera "Authorization"
                    String authHeader = request.getHeader("Authorization");

                    // 2. Si no hay token o no empieza por "Bearer ", deja pasar sin autenticar
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                              filterChain.doFilter(request, response);
                              return;
                    }

                    // 3. Extrae el token (quita "Bearer ")
                    String token = authHeader.substring(7);

                    // 4. Valida el token
                    if (jwtUtil.isTokenValid(token)) {
                              String email = jwtUtil.extractEmail(token);
                              String rol = jwtUtil.extractRol(token);

                              // 5. Crea el objeto de autenticación y lo pone en el contexto de Spring
                              UsernamePasswordAuthenticationToken auth =
                              new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                              );
                              auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                              SecurityContextHolder.getContext().setAuthentication(auth);
                    }

                    // 6. Continúa con el siguiente filtro
                    filterChain.doFilter(request, response);
          }
          
}
