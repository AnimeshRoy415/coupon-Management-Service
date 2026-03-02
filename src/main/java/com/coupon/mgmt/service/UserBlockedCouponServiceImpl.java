package com.coupon.mgmt.service;

import com.coupon.mgmt.entity.UserBlockedCoupon;
import com.coupon.mgmt.repository.UserBlockedCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserBlockedCouponServiceImpl implements UserBlockedCouponService{

    @Autowired
    private UserBlockedCouponRepository repository;

    @Transactional
    @Override
    public void blockCoupon(Long userId, Long couponId, String reason) {
        UserBlockedCoupon blockedCoupon = new UserBlockedCoupon(
                null,
                userId,
                couponId,
                LocalDateTime.now(),
                reason,
                true
        );
        repository.save(blockedCoupon);
    }

    @Transactional
    @Override
    public void unblockExpiredCoupons(int hours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
        List<UserBlockedCoupon> expired = repository.findByActiveTrueAndBlockedAtBefore(cutoff);

        for (UserBlockedCoupon coupon : expired) {
            coupon.setActive(false);
        }

        repository.saveAll(expired);
    }

    @Override
    public boolean isBlocked(Long userId, Long couponId) {
        List<UserBlockedCoupon> blocked = repository.findByUserIdAndCouponIdAndActiveTrue(userId, couponId);
        return !blocked.isEmpty();
    }
}
