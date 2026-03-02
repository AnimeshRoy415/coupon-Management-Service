package com.coupon.mgmt.scheduler;

import com.coupon.mgmt.service.UserBlockedCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockedCouponScheduler {

    private final UserBlockedCouponService blockedCouponService;

    // Runs every hour
    @Scheduled(cron = "0 0 * * * *")
    public void reactivateBlockedCoupons() {
        int unblockHours = 24; // unblock coupons blocked for more than 24 hours
        blockedCouponService.unblockExpiredCoupons(unblockHours);
        log.info("Blocked coupons older than {} hours have been reactivated.", unblockHours);
    }
}