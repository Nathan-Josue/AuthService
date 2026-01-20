package com.example.microservice.exception;

import com.example.microservice.DTOs.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("INVALID_CREDENTIALS")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({TokenExpiredException.class, ExpiredJwtException.class})
    public ResponseEntity<ErrorResponse> handleTokenExpired(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("TOKEN_EXPIRED")
            .message("Le token a expiré")
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({InvalidTokenException.class, JwtException.class})
    public ResponseEntity<ErrorResponse> handleInvalidToken(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("INVALID_TOKEN")
            .message("Token invalide")
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.CONFLICT.value())
            .error("EMAIL_ALREADY_EXISTS")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("RESOURCE_NOT_FOUND")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> ErrorResponse.ValidationError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .build())
            .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("VALIDATION_ERROR")
            .message("Erreur de validation")
            .path(request.getRequestURI())
            .details(validationErrors)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("INTERNAL_SERVER_ERROR")
            .message("Une erreur interne est survenue")
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
