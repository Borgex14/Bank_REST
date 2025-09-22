package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис для работы с JWT токенами.
 */
public interface JwtService {

    /**
     * Извлекает username из токена.
     */
    String extractUsername(String token);

    /**
     * Извлекает конкретное claim значение из токена.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Генерирует токен для UserDetails.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Генерирует токен для сущности User.
     */
    String generateToken(User user);

    /**
     * Генерирует токен для UserDetails с дополнительными claims.
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Генерирует токен для сущности User с дополнительными claims.
     */
    String generateToken(Map<String, Object> extraClaims, User user);

    /**
     * Проверяет валидность токена для пользователя.
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Проверяет, истек ли срок действия токена.
     */
    boolean isTokenExpired(String token);

    /**
     * Извлекает дату истечения срока действия токена.
     */
    Date extractExpiration(String token);

    /**
     * Инвалидирует токен.
     */
    void invalidateToken(String token);

    /**
     * Получает username из токена.
     */
    String getUsernameFromToken(String token);

    /**
     * Возвращает время жизни токена.
     */
    long getJwtExpiration();
}