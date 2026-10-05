package ioc.m13.clubpadel.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component 
public class JwtUtil {
          // Inyectamos las propiedades del archivo application.properties
          // secret: clave secreta para firmar el token
          // expiration: tiempo de expiración del token en milisegundos
          @Value ("${jwt.secret}")
          private String secret;
          @Value ("${jwt.expiration}")
          private long expiration;

          // Convertir secret a clave criptográfica
          private SecretKey getSigningKey() {
                    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
          }

          // Generar token JWT con el email del usuario y el rol como claims
          public String generateToken(String email, String rol) {
                    return Jwts.builder()
                              .subject(email)
                              .claim("rol", rol)
                              .issuedAt(new Date())
                              .expiration(new Date(System.currentTimeMillis() + expiration))
                              .signWith(getSigningKey())
                              .compact();
          }
          // Extraer email del token JWT
          public String extractEmail(String token) {
                    return parseClaims(token).getSubject();
          }
          // Extraer rol del token JWT
          public String extractRol(String token) {
                    return parseClaims(token).get("rol", String.class);
          }
          // Validar token JWT
          public boolean isTokenValid(String token) {
                    try {
                              parseClaims(token);
                              return true;
                    } catch (Exception e) {
                              return false;
                    }
          }
          // Parsear claims del token JWT
          private Claims parseClaims(String token) {
                    return Jwts.parser()
                              .verifyWith(getSigningKey())
                              .build()
                              .parseSignedClaims(token)
                              .getPayload();
          }
}
