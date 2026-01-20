package com.example.microservice.controller;

import com.example.microservice.entity.User;
import com.example.microservice.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    @Operation(summary = "Lister tous les utilisateurs",
               description = "Récupère la liste complète de tous les utilisateurs enregistrés")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
        @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public List<User> list() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID",
               description = "Récupère les détails d'un utilisateur spécifique par son identifiant UUID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public User get(@Parameter(description = "UUID de l'utilisateur", required = true)
                    @PathVariable UUID id) {
        return userRepo.findById(id).orElseThrow();
    }
}

