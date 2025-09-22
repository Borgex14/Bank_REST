package com.example.bankcards.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для основной страницы API.
 * Предоставляет информацию о API и ссылку на документацию.
 */
@RestController
public class HomeController {

    /**
     * Возвращает основную информацию о API.
     *
     * @return JSON с названием API, версией и ссылкой на документацию
     */
    @GetMapping({"/", "/api", "/api/"})
    public String home() {
        return "{\"message\": \"Bank Card Manager API\", \"version\": \"1.0.0\", \"docs\": \"/api/swagger-ui.html\"}";
    }
}