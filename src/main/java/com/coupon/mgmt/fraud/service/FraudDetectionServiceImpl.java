package com.coupon.mgmt.fraud.service;

import com.coupon.mgmt.fraud.model.FraudScore;
import com.coupon.mgmt.fraud.rule.FraudRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final List<FraudRule> rules;

    public FraudDetectionServiceImpl(List<FraudRule> rules) {
        for (FraudRule rule : rules) {
            System.out.println("Rule : "+ rule);        }
        this.rules = rules;
    }

    @Override
    public FraudScore evaluateUser(Long userId) {

        FraudScore fraudScore = new FraudScore(userId);


        for (FraudRule rule : rules) {
            rule.apply(userId, fraudScore);
        }

        fraudScore.evaluateLevel();
        return fraudScore;
    }

    @Override
    public boolean isFraudulent(Long userId) {

        FraudScore score = evaluateUser(userId);
        return score.getLevel().isBlocked();
    }
}