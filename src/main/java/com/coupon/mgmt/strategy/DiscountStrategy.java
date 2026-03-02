package com.coupon.mgmt.strategy;

import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.model.Cart;

public interface DiscountStrategy {
    CouponType getType();

    double applyDiscount(Coupon coupon, Cart cart);
}
