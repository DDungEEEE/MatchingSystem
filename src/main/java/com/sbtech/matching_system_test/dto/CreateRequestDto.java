package com.sbtech.matching_system_test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateRequestDto(
        @NotNull @JsonProperty("user_id") Long userId,
        @NotNull String address,
        @Size(max = 500) String description,
        // 기본 10km
        @Positive @JsonProperty("radius_km") Double radiusKm
) {}
