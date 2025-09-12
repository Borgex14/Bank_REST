package com.example.bankcards.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping({"/", "/api", "/api/"})
    public String home() {
        return "{\"message\": \"Bank Card Manager API\", \"version\": \"1.0.0\", \"docs\": \"/api/swagger-ui.html\"}";
    }
}