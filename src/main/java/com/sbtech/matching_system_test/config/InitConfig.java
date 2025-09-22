package com.sbtech.matching_system_test.config;

import com.sbtech.matching_system_test.domain.store.Store;
import com.sbtech.matching_system_test.domain.store.UserAccount;
import com.sbtech.matching_system_test.repository.RepairRequestRepository;
import com.sbtech.matching_system_test.repository.StoreRepository;
import com.sbtech.matching_system_test.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData(
            UserAccountRepository userAccountRepository,
            StoreRepository storeRepository
    ) {
        return args -> {
            // 1) 사용자 더미 데이터
            UserAccount user1 = UserAccount.builder()
                    .name("홍길동")
                    .loginId("user1")
                    .password(passwordEncoder.encode("1234"))
                    .phone("010-1111-1111")
                    .build();

            UserAccount user2 = UserAccount.builder()
                    .name("김철수")
                    .loginId("user2")
                    .password(passwordEncoder.encode("1234"))
                    .phone("010-2222-2222")
                    .build();

            userAccountRepository.saveAll(List.of(user1, user2));

            // 2) 매장 더미 데이터
            Store store1 = Store.builder()
                    .name("배터리프렌즈 강남점")
                    .phone("02-111-2222")
                    .loginId("store1")
                    .password(passwordEncoder.encode("abcd"))
                    .address("서울 강남구 테헤란로 123")
                    .latitude(37.4999)
                    .longitude(127.0364)
                    .active(true)
                    .build();

            Store store2 = Store.builder()
                    .name("배터리프렌즈 홍대점")
                    .phone("02-333-4444")
                    .loginId("store2")
                    .password(passwordEncoder.encode("abcd"))
                    .address("서울 마포구 양화로 45")
                    .latitude(37.5563)
                    .longitude(126.9220)
                    .active(true)
                    .build();

            Store store3 = Store.builder()
                    .name("배터리프렌즈 잠실점")
                    .phone("02-555-6666")
                    .loginId("store3")
                    .password(passwordEncoder.encode("abcd"))
                    .address("서울 송파구 올림픽로 240")
                    .active(true)
                    .build();

            storeRepository.saveAll(List.of(store1, store2, store3));


        };
    }
}