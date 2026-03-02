package com.coupon.mgmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_blocked_coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBlockedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long couponId;

    private LocalDateTime blockedAt;

    private String reason;

    private boolean active = true; // true = still blocked
}