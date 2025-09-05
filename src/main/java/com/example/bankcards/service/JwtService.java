package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(User user);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, User user); // Добавлен недостающий метод

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    void invalidateToken(String token);

    String getUsernameFromToken(String token);

    long getJwtExpiration();
}