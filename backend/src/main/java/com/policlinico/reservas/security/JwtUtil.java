package com.policlinico.reservas.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

// ─── JWT Utility ─────────────────────────────────────────

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generate(String email, Integer pacienteId) {
        return Jwts.builder()
                .subject(email)
                .claim("pacienteId", pacienteId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key())
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Integer extractPacienteId(String token) {
        return parseClaims(token).get("pacienteId", Integer.class);
    }

    public boolean isValid(String token) {
        try { parseClaims(token); return true; }
        catch (JwtException e) { return false; }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key()).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

// ─── JWT Filter ──────────────────────────────────────────

@Component
@RequiredArgsConstructor
class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.isValid(token)) {
                var auth = new UsernamePasswordAuthenticationToken(
                        jwtUtil.extractEmail(token), null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(req, res);
    }
}