package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
