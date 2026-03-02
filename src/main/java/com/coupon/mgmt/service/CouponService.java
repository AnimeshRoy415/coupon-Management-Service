package com.coupon.mgmt.service;

import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.dtos.response.CouponResponseDto;
import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.model.Cart;

import java.util.List;

public interface CouponService {
    Coupon createCoupon(CouponDto coupon);
    Coupon getCouponById(Long id);
    List<Coupon> getAllCoupons(Boolean isActive);
    Coupon updateCoupon(Long id, Coupon coupon);
    void deleteCoupon(Long id);
    List<CouponResponseDto> findApplicableCoupons(Cart cart);
    Cart applyCoupon(Long couponId, Cart cart);
    Cart applyMultipleCoupons(List<Long> couponIds, Cart cart); // New method to apply multiple coupons
    void recordCouponUsage(Cart cart, Long couponId, Double discountApplied);

}