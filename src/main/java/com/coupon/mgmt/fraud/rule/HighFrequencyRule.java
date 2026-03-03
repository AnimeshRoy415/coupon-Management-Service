package com.coupon.mgmt.fraud.rule;

import com.coupon.mgmt.entity.CouponUsageHistory;
import com.coupon.mgmt.fraud.model.FraudScore;
import com.coupon.mgmt.repository.CouponUsageHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class HighFrequencyRule implements FraudRule {

    @Autowired
    private CouponUsageHistoryRepository repository;

    @Override
    public void apply(Long userId, FraudScore fraudScore) {

        LocalDateTime oneHourAgo =
                LocalDateTime.now(ZoneOffset.UTC).minusHours(1);

        long count =
                repository.countByUserIdAndUsedAtAfter(userId, oneHourAgo);


        if (count > 5) {
            fraudScore.addRisk(40, "High coupon usage in last hour");
        }
    }

}