package com.example.bankcards.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("application", Map.of(
                "name", "Bank Card Manager",
                "version", "1.0.0",
                "description", "REST API for bank card management",
                "team", "Borgex Team"
        ));

        builder.withDetail("environment", Map.of(
                "springVersion", "3.2.0",
                "javaVersion", System.getProperty("java.version"),
                "profiles", "dev"
        ));
    }
}