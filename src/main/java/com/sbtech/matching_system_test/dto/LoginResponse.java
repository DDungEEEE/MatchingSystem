package com.sbtech.matching_system_test.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String type       // "USER" 또는 "STORE"
) {}