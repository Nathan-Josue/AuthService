package com.example.microservice.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse contenant les tokens JWT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(
    @Schema(description = "Token d'accès JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,

    @Schema(description = "Token de rafraîchissement", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken,

    @Schema(description = "Type de token", example = "Bearer")
    String tokenType,

    @Schema(description = "Durée de validité du token d'accès en millisecondes", example = "900000")
    Long expiresIn
) {
    public TokenResponse(String accessToken, String refreshToken, Long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }

    public TokenResponse(String accessToken) {
        this(accessToken, null, "Bearer", null);
    }
}
