package com.example.bankcards.config;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.secret-key:default-secret-key-123}")
    private String secretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AES256TextEncryptor textEncryptor() {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(secretKey);
        return encryptor;
    }

    public String encrypt(String data) {
        return textEncryptor().encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return textEncryptor().decrypt(encryptedData);
    }
}