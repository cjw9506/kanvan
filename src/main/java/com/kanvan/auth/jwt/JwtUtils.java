package com.kanvan.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret.access.key}")
    private String accessSecretKey;

    private final static Long EXPIRED_ACCESS_MS = 60 * 60 * 1000L;
    private final static String ACCESS_TOKEN = "accessToken";

    public String generateAccessToken(String account) {
        Claims claims = Jwts.claims();
        claims.put("type", ACCESS_TOKEN);
        claims.put("account", account);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRED_ACCESS_MS))
                .signWith(SignatureAlgorithm.HS512, accessSecretKey)
                .compact();

        return accessToken;
    }
}
