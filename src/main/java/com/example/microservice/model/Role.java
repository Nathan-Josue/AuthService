package com.example.microservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name; // ROLE_USER, ROLE_ADMIN, etc.

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

}
