package com.sbtech.matching_system_test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AcceptRequestDto(
        @NotNull @JsonProperty("store_id") Long storeId
) {}