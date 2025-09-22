package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для стандартизированного ответа об ошибке API.
 * Содержит подробную информацию об ошибке для клиента.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    /**
     * Временная метка возникновения ошибки
     */
    private LocalDateTime timestamp;

    /**
     * HTTP статус код ошибки
     */
    private int status;

    /**
     * Краткое описание типа ошибки
     */
    private String error;

    /**
     * Детальное сообщение об ошибке
     */
    private String message;

    /**
     * URL путь, по которому произошла ошибка
     */
    private String path;

    /**
     * Список ошибок валидации полей (если применимо)
     */
    private List<FieldError> fieldErrors;

    /**
     * DTO для описания ошибки валидации конкретного поля
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        /**
         * Название поля с ошибкой
         */
        private String field;

        /**
         * Сообщение об ошибке для поля
         */
        private String message;

        /**
         * Отклоненное значение, которое вызвало ошибку
         */
        private Object rejectedValue;
    }
}