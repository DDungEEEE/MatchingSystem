package com.sbtech.matching_system_test.domain.store;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60, nullable = false)
    private String name;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 120, nullable = false, unique = true)
    private String phone;

    // etc: 이메일, 주소 등
}