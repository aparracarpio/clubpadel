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

/**
 * JwtFilter, filtro que ejecutado con cada petición HTTP para validar el token JWT.
 *        Se encarga de:
 *       - Leer el header Authorization.
 *       - Validar el token JWT.
 *       - Si empieza por Bearer y es válido, extrae email y rol del token.
 */

@Component 
public class JwtFilter extends OncePerRequestFilter {
          // lógica para filtrar y validar el JWT en cada petición

          @Autowired
          private JwtUtil jwtUtil;

          // Este método se ejecuta para cada petición HTTP
          @Override
          protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
                    // Log de la petición entrante
                    String uri = request.getRequestURI();
                    System.out.println(">>> JWT FILTER: " + request.getMethod() + " " + uri);
                    // Leer el header Authorization
                    String authHeader = request.getHeader("Authorization");
                              if (authHeader == null) {
                              System.out.println(">>> SIN HEADER Authorization");
                              filterChain.doFilter(request, response);
                              return;
                    }

                    System.out.println(">>> Header: " + authHeader.substring(0, Math.min(50, authHeader.length())) + "...");
                    // Comprobar que el header empieza por "Bearer "
                    if (!authHeader.startsWith("Bearer ")) {
                              System.out.println(">>> No empieza por Bearer");
                              filterChain.doFilter(request, response);
                              return;
                    }
                    // Extraer el token JWT del header
                    String token = authHeader.substring(7);
                    boolean valido = jwtUtil.isTokenValid(token);
                    System.out.println(">>> Token válido: " + valido);
                    // Si el token es válido, extraer email y rol y establecer la autenticación en el contexto
                    if (valido) {
                              String email = jwtUtil.extractEmail(token);
                              String rol = jwtUtil.extractRol(token);
                              System.out.println(">>> Email: " + email + " | Rol: " + rol);

                              UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(
                                                  email,
                                                  null,
                                                  List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                                        );
                                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                        SecurityContextHolder.getContext().setAuthentication(auth);
                                        System.out.println(">>> Autenticación establecida en el contexto");
                    } else {
                              System.out.println(">>> Token INVÁLIDO — no se autentica");
                    }
                    filterChain.doFilter(request, response);
          }
          
}
