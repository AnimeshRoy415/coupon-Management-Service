package com.coupon.mgmt.service;

public interface UserBlockedCouponService {
    public void blockCoupon(Long userId, Long couponId, String reason);
    public void unblockExpiredCoupons(int hours);
    public boolean isBlocked(Long userId, Long couponId);
}
