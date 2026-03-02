package com.coupon.mgmt.dtos.response;

import com.coupon.mgmt.entity.CouponType;
import lombok.Data;

@Data
public class CouponResponseDto {

    private Long id;
    private CouponType type;
    private Double discount;
}
