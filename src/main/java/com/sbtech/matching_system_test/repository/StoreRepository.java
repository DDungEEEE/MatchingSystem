package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}