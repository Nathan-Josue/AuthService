package com.example.microservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id_user", columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true)
    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe_hash", nullable = false)
    private String motDePasseHash;

    @Column(name = "date_creation", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    @UpdateTimestamp
    private LocalDateTime dateModification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus statut = UserStatus.ACTIF;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", schema = "public",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Méthodes métier (business logic)

    /**
     * Vérifie si l'utilisateur a la permission spécifiée via ses rôles
     */
    public boolean hasPermission(String permission) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(permission));
    }

    /**
     * Vérifie si l'utilisateur est actif
     */
    public boolean isActif() {
        return this.statut == UserStatus.ACTIF && this.enabled;
    }

    /**
     * Active le compte utilisateur
     */
    public void activer() {
        this.statut = UserStatus.ACTIF;
        this.enabled = true;
    }

    /**
     * Désactive le compte utilisateur
     */
    public void desactiver() {
        this.statut = UserStatus.INACTIF;
        this.enabled = false;
    }

    /**
     * Suspend le compte utilisateur
     */
    public void suspendre() {
        this.statut = UserStatus.SUSPENDU;
        this.enabled = false;
    }
}
