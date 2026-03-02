package com.coupon.mgmt.fraud.rule;

import com.coupon.mgmt.fraud.model.FraudScore;
import com.coupon.mgmt.repository.CouponUsageHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RepeatedUsageRule implements FraudRule {

    @Autowired
    private CouponUsageHistoryRepository repository;


    @Override
    public void apply(Long userId, FraudScore fraudScore) {

//        long usageCount = repository.findAll().stream()
//                .filter(h -> h.getUserId().equals(userId))
//                .count();
        System.out.println(userId);

        long usageCount = repository.countByUserId(userId);


        if (usageCount > 20) {
            fraudScore.addRisk(30, "Repeated coupon abuse pattern");
        }
    }
}