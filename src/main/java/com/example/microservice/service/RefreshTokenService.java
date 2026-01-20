package com.example.microservice.service;

import com.example.microservice.entity.RefreshToken;
import com.example.microservice.entity.User;
import com.example.microservice.exception.InvalidTokenException;
import com.example.microservice.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String createRefreshToken(User user) {
        // Generate a unique token
        String token = UUID.randomUUID().toString();
        String tokenHash = passwordEncoder.encode(token);

        // Create token family for rotation tracking
        String tokenFamily = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .tokenHash(tokenHash)
            .tokenFamily(tokenFamily)
            .expiresAt(Instant.now().plusMillis(refreshTokenExpiration))
            .build();

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    @Transactional
    public RefreshToken verifyAndGetToken(String token) {
        // Find all non-revoked tokens and check against hash
        var allTokens = refreshTokenRepository.findAll();

        for (RefreshToken rt : allTokens) {
            if (!rt.isRevoked() && passwordEncoder.matches(token, rt.getTokenHash())) {
                if (rt.getExpiresAt().isBefore(Instant.now())) {
                    throw new InvalidTokenException("Le refresh token a expiré");
                }
                return rt;
            }
        }

        throw new InvalidTokenException("Refresh token invalide");
    }

    @Transactional
    public void revokeToken(String token) {
        RefreshToken refreshToken = verifyAndGetToken(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }

    @Transactional
    @Scheduled(cron = "0 0 2 * * *") // Run daily at 2 AM
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(Instant.now());
    }
}
