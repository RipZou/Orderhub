package com.orderhub.service;

import com.orderhub.domain.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${orderhub.jwt.secret}")
    private String secret;
    @Value("${orderhub.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(String userId, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId)// 把 userId 放进 token
                .claim("role", role.name())
                .issuedAt(now)             // 签发时间
                .expiration(expiry)        // 过期时间
                .signWith(getSigningKey()) // 用 secret 签名
                .compact();                // 变成字符串
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // 验签（篡改会抛异常）
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();                 // 就是 generateToken 里的 userId
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractUserId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


}
