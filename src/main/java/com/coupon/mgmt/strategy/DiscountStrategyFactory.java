package com.coupon.mgmt.strategy;

import com.coupon.mgmt.entity.CouponType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountStrategyFactory {

    private final List<DiscountStrategy> strategies;

    public DiscountStrategyFactory(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public DiscountStrategy getStrategy(CouponType type) {
        return strategies.stream()
                .filter(strategy -> strategy.getType() == type)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid Coupon Type"));
    }
}