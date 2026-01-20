package com.example.microservice.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn
) {
    public TokenResponse(String accessToken, String refreshToken, Long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }

    public TokenResponse(String accessToken) {
        this(accessToken, null, "Bearer", null);
    }
}
