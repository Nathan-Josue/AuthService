package com.example.microservice.service;

import com.example.microservice.DTOs.LoginRequest;
import com.example.microservice.DTOs.RefreshTokenRequest;
import com.example.microservice.DTOs.SignupRequest;
import com.example.microservice.DTOs.TokenResponse;
import com.example.microservice.entity.RefreshToken;
import com.example.microservice.entity.Role;
import com.example.microservice.entity.User;
import com.example.microservice.entity.UserStatus;
import com.example.microservice.exception.EmailAlreadyExistsException;
import com.example.microservice.exception.InvalidCredentialsException;
import com.example.microservice.exception.ResourceNotFoundException;
import com.example.microservice.repository.RoleRepository;
import com.example.microservice.repository.UserRepository;
import com.example.microservice.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public TokenResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new ResourceNotFoundException("Rôle USER introuvable"));

        User user = User.builder()
            .email(request.getEmail())
            .motDePasseHash(passwordEncoder.encode(request.getPassword()))
            .nom(request.getLastName())
            .prenom(request.getFirstName())
            .telephone(request.getTelephone())
            .statut(UserStatus.ACTIF)
            .enabled(true)
            .roles(Set.of(userRole))
            .build();

        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken, accessTokenExpiration);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.password(), user.getMotDePasseHash())) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Compte désactivé");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken, accessTokenExpiration);
    }

    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyAndGetToken(request.refreshToken());
        User user = refreshToken.getUser();

        // Revoke old refresh token (token rotation)
        refreshTokenService.revokeToken(request.refreshToken());

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(newAccessToken, newRefreshToken, accessTokenExpiration);
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.refreshToken());
    }
}
