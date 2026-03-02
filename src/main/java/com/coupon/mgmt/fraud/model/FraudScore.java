package com.coupon.mgmt.fraud.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FraudScore {

    private Long userId;
    private double score;
    private FraudLevel level;
    private List<String> riskFactors;

    public FraudScore(Long userId) {
        this.userId = userId;
        this.score = 0;
        this.riskFactors = new ArrayList<>();
        this.level = FraudLevel.LOW;
    }

    public void addRisk(double points, String reason) {
        this.score += points;
        this.riskFactors.add(reason);
    }

    public void evaluateLevel() {
        if (score >= 90) level = FraudLevel.CRITICAL;
        else if (score >= 60) level = FraudLevel.HIGH;
        else if (score >= 30) level = FraudLevel.MEDIUM;
        else level = FraudLevel.LOW;
    }
}