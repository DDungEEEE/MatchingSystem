package com.sbtech.matching_system_test.domain.store;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stores",
        indexes = {
                @Index(name = "idx_store_lat_lng", columnList = "latitude,longitude")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String name;

    @Column(length = 20, nullable = false, unique = true)
    private String phone;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    // 위도/경도 (WGS84)
    @Column(nullable = false)
    private Double latitude;   // 예: 37.5665

    @Column(nullable = false)
    private Double longitude;  // 예: 126.9780

    @Column(nullable = false)
    private boolean active;    // 매장 영업/사용 가능 여부
}