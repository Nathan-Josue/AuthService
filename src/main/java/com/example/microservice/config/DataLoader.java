package com.example.microservice.config;

import com.example.microservice.entity.Role;
import com.example.microservice.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("Initialisation des données de base...");

        // Créer les rôles par défaut s'ils n'existent pas
        createRoleIfNotExists("ROLE_USER", "Utilisateur standard");
        createRoleIfNotExists("ROLE_ADMIN", "Administrateur");

        logger.info("Initialisation des données terminée");
    }

    private void createRoleIfNotExists(String roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .description(description)
                    .build();
            roleRepository.save(role);
            logger.info("Rôle créé : {}", roleName);
        } else {
            logger.info("Rôle existant : {}", roleName);
        }
    }
}
