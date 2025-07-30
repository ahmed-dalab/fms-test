package com.ahmed.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "qwertyuioplkiujyhtgrfedwsqazxscdvfbgnhmjklkjuiolkytfbmj";
    private final long EXPIRATION_TIME = 86400000; // 1 day in ms

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String extractEmailFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token header");
        }
        String token = authHeader.substring(7);
        return new JwtUtil().extractEmail(token); // or inject if needed
    }
    public String extractRole(String token) {
        System.out.println("token inside extractRole: " + token);

        // Remove "Bearer " if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim(); // remove Bearer and trim whitespace
        }

        return parseClaims(token).get("role", String.class);
    }


    public String generateToken(Long id,String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", id) // ✅ add user ID claim
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token.trim()).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
