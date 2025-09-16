package com.sbtech.matching_system_test.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long accessTokenValidity = 1000 * 60 * 30;   // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    public String generateAccessToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String getLoginId(String token) {
        return validateToken(token).getBody().getSubject();
    }
}