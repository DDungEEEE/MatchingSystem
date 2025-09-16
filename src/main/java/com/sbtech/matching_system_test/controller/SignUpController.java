package com.sbtech.matching_system_test.controller;

import com.sbtech.matching_system_test.domain.store.Store;
import com.sbtech.matching_system_test.domain.store.UserAccount;
import com.sbtech.matching_system_test.dto.StoreSignUpRequest;
import com.sbtech.matching_system_test.dto.UserSignUpRequest;
import com.sbtech.matching_system_test.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;

    @PostMapping("/user")
    public ResponseEntity<UserAccount> userSignUp(@RequestBody UserSignUpRequest request){
        return ResponseEntity.ok(signUpService.registerUser(request));
    }

    @PostMapping("/store")
    public ResponseEntity<Store> storeSignUp(@RequestBody StoreSignUpRequest request) throws IOException {
        return ResponseEntity.ok(signUpService.registerStore(request));
    }
}
