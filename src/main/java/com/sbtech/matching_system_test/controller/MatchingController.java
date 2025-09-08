package com.sbtech.matching_system_test.controller;

import com.sbtech.matching_system_test.domain.Store;
import com.sbtech.matching_system_test.dto.AcceptRequestDto;
import com.sbtech.matching_system_test.dto.CreateRequestDto;
import com.sbtech.matching_system_test.dto.RepairRequestResultDto;
import com.sbtech.matching_system_test.service.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    // 1) 사용자 수리요청 생성
    @PostMapping("/requests")
    public RepairRequestResultDto createRequest(@Valid @RequestBody CreateRequestDto dto) {
        return matchingService.createRequest(dto);
    }

    // 2) (선택) 반경 내 후보 매장 조회 (디버그/테스트용)
    @GetMapping("/stores/nearby")
    public List<Store> findNearbyStores(@RequestParam double lat,
                                        @RequestParam double lng,
                                        @RequestParam(defaultValue = "10") double radiusKm,
                                        @RequestParam(defaultValue = "50") int limit) {
        return matchingService.findNearbyStores(lat, lng, radiusKm, limit);
    }

    // 3) 매장 측 수락
    @PostMapping("/requests/{requestId}/accept")
    public RepairRequestResultDto acceptRequest(@PathVariable Long requestId,
                                                @Valid @RequestBody AcceptRequestDto dto) {
        return matchingService.acceptRequest(requestId, dto.storeId());
    }

    // 4) 요청 단건 조회
    @GetMapping("/requests/{requestId}")
    public RepairRequestResultDto getRequest(@PathVariable Long requestId) {
        return matchingService.getRequest(requestId);
    }

    // 5) (가게 화면) 반경 내 대기중 요청 리스트
    @GetMapping("/stores/{storeId}/requests")
    public List<RepairRequestResultDto> getPendingRequestsForStore(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "10") double radiusKm
    ) {
        return matchingService.getPendingRequestsForStore(storeId, radiusKm);
    }
}
