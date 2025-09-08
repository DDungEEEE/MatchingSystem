package com.sbtech.matching_system_test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

public record CreateRequestDto(
        @NotNull @JsonProperty("user_id") Long userId,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @Size(max = 500) String description,
        // 기본 10km
        @Positive @JsonProperty("radius_km") Double radiusKm
) {}
