package com.coupon.mgmt.validation;

import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.entity.CouponType;

public interface CouponValidator {
    CouponType getType();

    void validate(CouponDto dto);

}
