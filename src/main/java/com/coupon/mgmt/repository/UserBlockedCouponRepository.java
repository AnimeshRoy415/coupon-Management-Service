package com.coupon.mgmt.repository;

import com.coupon.mgmt.entity.UserBlockedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserBlockedCouponRepository extends JpaRepository<UserBlockedCoupon, Long> {

    List<UserBlockedCoupon> findByActiveTrue();

    List<UserBlockedCoupon> findByUserIdAndCouponIdAndActiveTrue(Long userId, Long couponId);

    List<UserBlockedCoupon> findByActiveTrueAndBlockedAtBefore(LocalDateTime dateTime);
}