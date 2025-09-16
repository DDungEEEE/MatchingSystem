package com.sbtech.matching_system_test.domain.store;

import com.sbtech.matching_system_test.domain.repaire.RepairRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_acceptances",
        uniqueConstraints = @UniqueConstraint(name = "uk_request_store", columnNames = {"request_id","store_id"}),
        indexes = {
                @Index(name = "idx_accept_request", columnList = "request_id"),
                @Index(name = "idx_accept_store", columnList = "store_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreAcceptance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id")
    private RepairRequest request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AcceptanceStatus status; // PENDING -> WITHDRAWN 등

    @CreationTimestamp
    private LocalDateTime createdAt;
}