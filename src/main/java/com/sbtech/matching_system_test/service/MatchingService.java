package com.sbtech.matching_system_test.service;

import com.sbtech.matching_system_test.domain.*;
import com.sbtech.matching_system_test.dto.CreateRequestDto;
import com.sbtech.matching_system_test.dto.RepairRequestResultDto;
import com.sbtech.matching_system_test.repository.RepairRequestRepository;
import com.sbtech.matching_system_test.repository.StoreRepository;
import com.sbtech.matching_system_test.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserAccountRepository userRepo;
    private final StoreRepository storeRepo;
    private final RepairRequestRepository requestRepo;

    /** 1) 요청 생성 + 반경 내 매장들에게 실시간 알림 */
    @Transactional
    public RepairRequestResultDto createRequest(CreateRequestDto dto) {
        UserAccount user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RepairRequest req = RepairRequest.builder()
                .user(user)
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .description(dto.description())
                .status(RequestStatus.PENDING)
                .build();
        requestRepo.save(req);

        // 반경 기본값 처리
        double radiusKm = dto.radiusKm() != null ? dto.radiusKm() : 10.0;

        // 반경 내 매장 찾기
        List<Store> nearby = storeRepo.findAll().stream()
                .filter(s -> distanceKm(dto.latitude(), dto.longitude(),
                        s.getLatitude(), s.getLongitude()) <= radiusKm)
                .toList();

        // 각 매장 구독 채널로 알림 전송
        nearby.forEach(store ->
                messagingTemplate.convertAndSend(
                        "/topic/stores/" + store.getId() + "/requests", toDto(req))
        );

        return toDto(req);
    }

    /** 2) 매장 수락 + 사용자에게 실시간 매칭 알림 */
    @Transactional
    public RepairRequestResultDto acceptRequest(Long requestId, Long storeId) {
        RepairRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        if (req.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Already matched or closed");
        }

        req.setStatus(RequestStatus.MATCHED);
        req.setMatchedStore(store);

        RepairRequestResultDto result = toDto(req);

        // 사용자 구독 채널로 매칭 성공 전송
        messagingTemplate.convertAndSend(
                "/topic/users/" + req.getUser().getId() + "/matched", result);

        return result;
    }

    /** 3) 단건 조회 (컨트롤러에서 호출) */
    @Transactional(readOnly = true)
    public RepairRequestResultDto getRequest(Long requestId) {
        RepairRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        return toDto(req);
    }

    /** 4) 반경 내 매장 조회 (디버그/테스트) */
    @Transactional(readOnly = true)
    public List<Store> findNearbyStores(double lat, double lng, double radiusKm, int limit) {
        return storeRepo.findAll().stream()
                .filter(s -> distanceKm(lat, lng, s.getLatitude(), s.getLongitude()) <= radiusKm)
                .limit(limit)
                .toList();
    }

    /** 5) 특정 매장 기준 반경 내 PENDING 요청 목록 (가게 화면용) */
    @Transactional(readOnly = true)
    public List<RepairRequestResultDto> getPendingRequestsForStore(Long storeId, double radiusKm) {
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        // (간단 구현) 전체에서 필터 – 트래픽 많아지면 전용 쿼리로 최적화 권장
        return requestRepo.findAll().stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .filter(r -> distanceKm(r.getLatitude(), r.getLongitude(),
                        store.getLatitude(), store.getLongitude()) <= radiusKm)
                .map(this::toDto)
                .toList();
    }

    /** 공용 DTO 변환 */
    private RepairRequestResultDto toDto(RepairRequest r) {
        return new RepairRequestResultDto(
                r.getId(),
                r.getUser().getId(),
                r.getLatitude(),
                r.getLongitude(),
                r.getDescription(),
                r.getStatus(),
                r.getMatchedStore() != null ? r.getMatchedStore().getId() : null,
                r.getMatchedStore() != null ? r.getMatchedStore().getName() : null
        );
    }

    /** 거리 계산(Haversine) */
    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
