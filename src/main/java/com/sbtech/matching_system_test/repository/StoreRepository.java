package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByLoginId(String loginId);
}