package com.sbtech.matching_system_test.dto;

public record UserSignUpRequest(
        String name,
        String loginId,
        String password,
        String phone
) {}
