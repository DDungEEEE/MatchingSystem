package com.sbtech.matching_system_test.repository;

import com.sbtech.matching_system_test.domain.RepairRequest;
import com.sbtech.matching_system_test.domain.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    Optional<RepairRequest> findByIdAndStatus(Long id, RequestStatus status);
}
