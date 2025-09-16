package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.store.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByLoginId(String loginId);
}
