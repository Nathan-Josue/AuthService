package com.example.microservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requête de rafraîchissement ou de révocation de token")
public record RefreshTokenRequest(
    @Schema(description = "Token de rafraîchissement", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    @NotBlank(message = "Le refresh token est obligatoire")
    String refreshToken
) {}
