package com.example.microservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requête de connexion")
public record LoginRequest(
    @Schema(description = "Adresse email de l'utilisateur", example = "jean.dupont@exemple.com", required = true)
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    String email,

    @Schema(description = "Mot de passe de l'utilisateur", example = "MotDePasse123!", required = true)
    @NotBlank(message = "Le mot de passe est obligatoire")
    String password
) {}
