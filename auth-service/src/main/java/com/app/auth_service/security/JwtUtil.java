package com.app.auth_service.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.app.auth_service.config.AppProperties;
import com.app.auth_service.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final AppProperties properties;
    private final SecretKey key;

    public JwtUtil(AppProperties properties) {
        this.properties = properties;

        String secret = properties.getSecurity().getJwt().getSecret();
        if (!StringUtils.hasText(secret) || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be set and at least 32 characters long");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Date issuedAt = new Date();
        Date expiry = new Date(issuedAt.getTime() + properties.getSecurity().getJwt().getExpirationMs());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("name", user.getName())
                .claim("picture", user.getPicture())
                .claim("provider", user.getProvider())
                .issuedAt(issuedAt)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
