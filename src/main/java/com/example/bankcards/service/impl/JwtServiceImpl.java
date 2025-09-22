package com.example.bankcards.service.impl;

import com.example.bankcards.entity.User;
import com.example.bankcards.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Реализация сервиса для работы с JWT токенами.
 * Обеспечивает генерацию, валидацию и управление JWT токенами.
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    String secretKey;

    @Value("${jwt.expiration}")
    long jwtExpiration;

    private final Map<String, Date> invalidatedTokens = new ConcurrentHashMap<>();

    /**
     * Извлекает username из JWT токена.
     *
     * @param token JWT токен
     * @return username из токена
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает конкретное claim значение из JWT токена.
     *
     * @param token JWT токен
     * @param claimsResolver функция для извлечения конкретного claim
     * @param <T> тип возвращаемого значения
     * @return значение claim
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Генерирует JWT токен для UserDetails.
     *
     * @param userDetails данные пользователя
     * @return JWT токен
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Генерирует JWT токен для сущности User с дополнительными claims.
     *
     * @param user сущность пользователя
     * @return JWT токен
     */
    @Override
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("email", user.getEmail());

        return generateToken(extraClaims, user);
    }

    /**
     * Генерирует JWT токен для UserDetails с дополнительными claims.
     *
     * @param extraClaims дополнительные claims для включения в токен
     * @param userDetails данные пользователя
     * @return JWT токен
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Генерирует JWT токен для сущности User с дополнительными claims.
     *
     * @param extraClaims дополнительные claims для включения в токен
     * @param user сущность пользователя
     * @return JWT токен
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    /**
     * Проверяет валидность JWT токена для указанного пользователя.
     *
     * @param token JWT токен
     * @param userDetails данные пользователя
     * @return true если токен валиден, false в противном случае
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(token) &&
                !isTokenInvalidated(token);
    }

    /**
     * Проверяет, истек ли срок действия JWT токена.
     *
     * @param token JWT токен
     * @return true если токен истек, false в противном случае
     */
    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия токена.
     *
     * @param token JWT токен
     * @return дата истечения срока действия
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Инвалидирует JWT токен (добавляет в черный список).
     *
     * @param token JWT токен для инвалидации
     */
    @Override
    public void invalidateToken(String token) {
        Date expiration = extractExpiration(token);
        invalidatedTokens.put(token, expiration);
        cleanInvalidatedTokens();
    }

    /**
     * Получает username из токена (синоним для extractUsername).
     *
     * @param token JWT токен
     * @return username из токена
     */
    @Override
    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }

    /**
     * Возвращает время жизни JWT токена в миллисекундах.
     *
     * @return время жизни токена
     */
    @Override
    public long getJwtExpiration() {
        return jwtExpiration;
    }

    /**
     * Строит JWT токен для UserDetails.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Строит JWT токен для сущности User.
     */
    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Извлекает все claims из JWT токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает ключ для подписи JWT токенов.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Проверяет, инвалидирован ли токен.
     */
    boolean isTokenInvalidated(String token) {
        return invalidatedTokens.containsKey(token);
    }

    /**
     * Очищает черный список токенов от истекших токенов.
     */
    private void cleanInvalidatedTokens() {
        Date now = new Date();
        invalidatedTokens.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}