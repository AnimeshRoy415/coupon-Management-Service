package com.coupon.mgmt.scheduler;

import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponExpiryScheduler {


    @Autowired
    private CouponRepository couponRepository;

    @Scheduled(cron = "0 0 0/12 * * ?")
    @Transactional
    public void autoExpireCoupons() {

        long currentTime = System.currentTimeMillis();

        int expiredCount =
                couponRepository.expireCoupons(currentTime);

        log.info("Auto-expire job executed. {} coupons expired.", expiredCount);
    }
}
