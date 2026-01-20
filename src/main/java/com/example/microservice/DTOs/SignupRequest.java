package com.example.microservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Requête d'inscription d'un nouvel utilisateur")
public class SignupRequest {

    @Schema(description = "Adresse email de l'utilisateur", example = "jean.dupont@exemple.com", required = true)
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Schema(description = "Mot de passe (minimum 8 caractères)", example = "MotDePasse123!", required = true)
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @Schema(description = "Prénom de l'utilisateur", example = "Jean", required = true)
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String firstName;

    @Schema(description = "Nom de famille de l'utilisateur", example = "Dupont", required = true)
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String lastName;

    @Schema(description = "Numéro de téléphone (optionnel)", example = "0612345678")
    @Size(min = 10, max = 15, message = "Le numéro de téléphone doit contenir entre 10 et 15 caractères")
    private String telephone;
}
