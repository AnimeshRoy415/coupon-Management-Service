package com.coupon.mgmt.fraud.model;

public enum FraudLevel {

    LOW(false),
    MEDIUM(false),
    HIGH(true),
    CRITICAL(true);

    private final boolean blocked;

    FraudLevel(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
