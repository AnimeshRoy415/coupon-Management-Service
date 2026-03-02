package com.coupon.mgmt.service;

import com.coupon.mgmt.dtos.response.CouponUsageHistResponseDto;

import java.util.List;

public interface CouponHistoryService {
    List<CouponUsageHistResponseDto> getAllCouponUsageHistory();

    CouponUsageHistResponseDto getCouponUsageHistoryById(Long id);

    List<CouponUsageHistResponseDto> getCouponUsageHistoryListById(List<Long> ids);

}
