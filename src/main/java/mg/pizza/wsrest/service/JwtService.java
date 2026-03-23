package mg.pizza.wsrest.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mg.pizza.wsrest.model.Role;

@Service
public class JwtService {
    private String SECRET = "my_secret_key-1234567890-abcdefghijklmnopqrstuvwxyz";

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String phone, Role role) {
        String roleValue = role != null ? role.name() : "CUSTOMER";

        return Jwts.builder()
                .subject(phone)
                .claims(Map.of(
                        "phone", phone,
                "role", roleValue))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractPhone(String token) {
        Claims claims = extractAllClaims(token);
        String phone = claims.get("phone", String.class);
        return phone != null ? phone : claims.getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
