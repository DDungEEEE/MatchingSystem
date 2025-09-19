package com.sbtech.matching_system_test.service;

import com.sbtech.matching_system_test.dto.LoginRequest;
import com.sbtech.matching_system_test.dto.LoginResponse;
import com.sbtech.matching_system_test.provider.JwtProvider;
import com.sbtech.matching_system_test.repository.StoreRepository;
import com.sbtech.matching_system_test.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String loginId = request.loginId();
        String password = request.password();

        // 1) UserAccount 먼저 조회
        var userOpt = userAccountRepository.findByLoginId(loginId);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            String accessToken = jwtProvider.generateAccessToken(user.getLoginId(), user.getId(), "USER");
            String refreshToken = jwtProvider.generateRefreshToken(user.getLoginId());
            return new LoginResponse(accessToken, refreshToken, "USER");
        }

        // 2) Store 조회
        var storeOpt = storeRepository.findByLoginId(loginId);
        if (storeOpt.isPresent()) {
            var store = storeOpt.get();
            if (!passwordEncoder.matches(password, store.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            String accessToken = jwtProvider.generateAccessToken(store.getLoginId(), store.getId(), "STORE");
            String refreshToken = jwtProvider.generateRefreshToken(store.getLoginId());
            return new LoginResponse(accessToken, refreshToken, "STORE");
        }

        // 3) 둘 다 없으면 예외
        throw new IllegalArgumentException("존재하지 않는 계정입니다.");
    }
}