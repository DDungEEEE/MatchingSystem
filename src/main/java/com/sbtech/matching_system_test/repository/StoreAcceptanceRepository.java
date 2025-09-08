package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.StoreAcceptance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreAcceptanceRepository extends JpaRepository<StoreAcceptance, Long> {
    boolean existsByRequest_IdAndStore_Id(Long requestId, Long storeId);
}