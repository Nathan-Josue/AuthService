package com.example.microservice.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ValidationError> details;

    @Data
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
    }
}
