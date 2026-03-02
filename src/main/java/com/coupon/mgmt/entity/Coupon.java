package com.coupon.mgmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Embedded
    private DiscountDetails details;

    private String code;

    private Long expirationDate;

    private int repetitionLimit; // For BxGy type
    private boolean active = true;

    private int redemptionCount = 0;
    private double totalRevenueGenerated = 0.0;
    private boolean stackable = true;

    private Integer maxUsagePerUser;
//    public boolean isValid() {
//        return expirationDate == null || new Date().before(expirationDate);
//    }

    public boolean isExpired() {
        return this.expirationDate != null &&
                this.expirationDate < System.currentTimeMillis();
    }

    public boolean isAvailable() {
        return repetitionLimit == 0 || redemptionCount < repetitionLimit;
    }

    public void incrementRedemptionCount() {
        this.redemptionCount++;
    }

    public void addToRevenue(double revenue) {
        this.totalRevenueGenerated += revenue;
    }


}