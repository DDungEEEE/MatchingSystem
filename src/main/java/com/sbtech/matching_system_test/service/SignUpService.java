package com.sbtech.matching_system_test.service;

import com.sbtech.matching_system_test.domain.store.Store;
import com.sbtech.matching_system_test.domain.store.UserAccount;
import com.sbtech.matching_system_test.dto.StoreSignUpRequest;
import com.sbtech.matching_system_test.dto.UserSignUpRequest;
import com.sbtech.matching_system_test.repository.StoreRepository;
import com.sbtech.matching_system_test.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeoCodingService geoCodingService;

    private void validateLoginIdUnique(String loginId) {
        if (userAccountRepository.findByLoginId(loginId).isPresent()
                || storeRepository.findByLoginId(loginId).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 loginId 입니다.");
        }
    }

    /**
     * 사용자 회원가입
     */
    @Transactional
    public UserAccount registerUser(UserSignUpRequest dto) {
        validateLoginIdUnique(dto.loginId());

        UserAccount user = UserAccount.builder()
                .name(dto.name())
                .loginId(dto.loginId())
                .password(passwordEncoder.encode(dto.password()))
                .phone(dto.phone())
                .build();

        UserAccount saved = userAccountRepository.save(user);

        return saved;
    }

    /**
     * 매장 회원가입
     */
    @Transactional
    public Store registerStore(StoreSignUpRequest dto) throws IOException {
        validateLoginIdUnique(dto.loginId());

        Map<String, Double> point = geoCodingService.getLocationByAd(dto.address());

        Store store = Store.builder()
                .name(dto.name())
                .phone(dto.phone())
                .loginId(dto.loginId())
                .password(passwordEncoder.encode(dto.password()))
                .address(dto.address())
                .latitude(point.get("lat"))
                .longitude(point.get("lng"))
                .active(true)
                .build();

        return storeRepository.save(store);

    }
}
