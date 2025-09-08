package com.sbtech.matching_system_test.dto;

import com.sbtech.matching_system_test.domain.RepairRequest;
import com.sbtech.matching_system_test.domain.RequestStatus;

public record RepairRequestResultDto(
        Long id,
        Long userId,
        Double latitude,
        Double longitude,
        String description,
        RequestStatus status,
        Long matchedStoreId,
        String matchedStoreName   // ✅ 매칭된 매장 이름 추가
) {
    private RepairRequestResultDto toDto(RepairRequest r) {
        return new RepairRequestResultDto(
                r.getId(),
                r.getUser().getId(),
                r.getLatitude(),
                r.getLongitude(),
                r.getDescription(),
                r.getStatus(),
                r.getMatchedStore() != null ? r.getMatchedStore().getId() : null,
                r.getMatchedStore() != null ? r.getMatchedStore().getName() : null  // ✅ 이름 반환
        );
    }

}