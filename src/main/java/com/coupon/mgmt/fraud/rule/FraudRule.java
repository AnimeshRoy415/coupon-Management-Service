package com.coupon.mgmt.fraud.rule;

import com.coupon.mgmt.fraud.model.FraudScore;

public interface FraudRule {
    void apply(Long userId, FraudScore fraudScore);

}
