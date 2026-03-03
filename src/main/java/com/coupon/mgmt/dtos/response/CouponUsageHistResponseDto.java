package com.coupon.mgmt.dtos.response;

import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.entity.Coupon;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CouponUsageHistResponseDto {

    private Long id;
    private Coupon coupon;
    private Cart cart;
    private LocalDateTime usedAt;
    private Double discountApplied;
    private Long userId;
}
