package com.example.microservice.controller;

import com.example.microservice.entity.User;
import com.example.microservice.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<User> list() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable UUID id) {
        return userRepo.findById(id).orElseThrow();
    }
}

