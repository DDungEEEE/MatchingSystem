package com.sbtech.matching_system_test.dto;

public record StoreSignUpRequest(
        String name,
        String phone,
        String loginId,
        String password,
        String address
) {}
