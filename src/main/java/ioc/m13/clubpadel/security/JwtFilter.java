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

          String uri = request.getRequestURI();
          System.out.println(">>> JWT FILTER: " + request.getMethod() + " " + uri);

          String authHeader = request.getHeader("Authorization");
          if (authHeader == null) {
          System.out.println(">>> SIN HEADER Authorization");
          filterChain.doFilter(request, response);
          return;
          }
          System.out.println(">>> Header: " + authHeader.substring(0, Math.min(50, authHeader.length())) + "...");

          if (!authHeader.startsWith("Bearer ")) {
          System.out.println(">>> No empieza por Bearer");
          filterChain.doFilter(request, response);
          return;
          }

          String token = authHeader.substring(7);
          boolean valido = jwtUtil.isTokenValid(token);
          System.out.println(">>> Token válido: " + valido);

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
