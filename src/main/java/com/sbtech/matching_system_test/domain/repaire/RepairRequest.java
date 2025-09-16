package com.sbtech.matching_system_test.domain.repaire;

import com.sbtech.matching_system_test.domain.store.UserAccount;
import com.sbtech.matching_system_test.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_requests",
        indexes = {
                @Index(name = "idx_request_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    // 요청 위치
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status;

    // 최종 매칭된 매장 (확정 후 세팅)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_store_id")
    private Store matchedStore;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 낙관적 락 버전
    @Version
    private Long version;
}