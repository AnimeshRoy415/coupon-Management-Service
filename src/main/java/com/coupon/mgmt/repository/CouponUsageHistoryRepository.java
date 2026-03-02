package com.coupon.mgmt.repository;

import com.coupon.mgmt.entity.CouponUsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponUsageHistoryRepository extends JpaRepository<CouponUsageHistory, Long> {

    @Query("SELECT COUNT(cuh) FROM CouponUsageHistory cuh " +
            "WHERE cuh.coupon.id = :couponId AND cuh.userId = :userId")
    long countByCouponIdAndUserId(@Param("couponId") Long couponId,
                                  @Param("userId") Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndUsedAtAfter(Long userId, LocalDateTime time);

    List<CouponUsageHistory> findByUserId(Long userId);
}
