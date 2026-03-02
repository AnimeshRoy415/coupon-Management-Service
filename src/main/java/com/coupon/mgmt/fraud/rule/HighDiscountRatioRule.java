package com.coupon.mgmt.fraud.rule;

import com.coupon.mgmt.entity.CouponUsageHistory;
import com.coupon.mgmt.fraud.model.FraudScore;
import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.repository.CouponUsageHistoryRepository;
import com.coupon.mgmt.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HighDiscountRatioRule implements FraudRule {

    private final CouponUsageHistoryRepository repository;

    public HighDiscountRatioRule(CouponUsageHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void apply(Long userId, FraudScore fraudScore) {

        List<CouponUsageHistory> histories =
                repository.findByUserId(userId);

        double totalDiscount = 0;
        double totalCartValue = 0;

        for (CouponUsageHistory history : histories) {

            Cart cart = JsonUtils.deserializeClass(
                    history.getCart(),
                    new TypeReference<Cart>() {}
            );

            totalDiscount += history.getDiscountApplied();
            totalCartValue += cart.getTotalPrice();
        }
        System.out.println("totalDiscount   :::  "+ totalDiscount);
        System.out.println("totalCartValue   ::: "+ totalCartValue);

        if (totalCartValue > 0) {

            double ratio = totalDiscount / totalCartValue;

            if (ratio > 0.7) {
                fraudScore.addRisk(35, "Abnormally high discount ratio");
            }
        }
    }
}