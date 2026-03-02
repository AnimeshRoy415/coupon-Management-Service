package com.coupon.mgmt.fraud.service;


import com.coupon.mgmt.fraud.model.FraudScore;

public interface FraudDetectionService {

    FraudScore evaluateUser(Long userId);

    boolean isFraudulent(Long userId);
}