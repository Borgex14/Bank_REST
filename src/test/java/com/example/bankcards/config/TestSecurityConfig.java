package com.example.bankcards.config;

import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService) {
            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                return true;
            }
        };
    }

    @Bean
    @Primary
    public JwtService jwtService() {
        return new JwtService() {
            @Override
            public String extractUsername(String token) {
                return "test-user";
            }

            @Override
            public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
                return null;
            }

            @Override
            public String generateToken(UserDetails userDetails) {
                return "test-token";
            }

            @Override
            public String generateToken(com.example.bankcards.entity.User user) {
                return "test-token";
            }

            @Override
            public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
                return "test-token";
            }

            @Override
            public String generateToken(Map<String, Object> extraClaims, com.example.bankcards.entity.User user) {
                return "test-token";
            }

            @Override
            public boolean isTokenValid(String token, UserDetails userDetails) {
                return true;
            }

            @Override
            public boolean isTokenExpired(String token) {
                return false;
            }

            @Override
            public Date extractExpiration(String token) {
                return new Date(System.currentTimeMillis() + 3600000);
            }

            @Override
            public void invalidateToken(String token) {
            }

            @Override
            public String getUsernameFromToken(String token) {
                return "test-user";
            }

            @Override
            public long getJwtExpiration() {
                return 3600000;
            }
        };
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return User.withUsername("test-user")
                        .password("password")
                        .roles("USER")
                        .build();
            }
        };
    }
}