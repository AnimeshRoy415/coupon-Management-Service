package com.coupon.mgmt.controller;

import com.coupon.mgmt.dtos.response.ApiResponse;
import com.coupon.mgmt.dtos.response.CouponUsageHistResponseDto;
import com.coupon.mgmt.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons/history")
@RequiredArgsConstructor
public class CouponUsageHistoryController {

    @Autowired
    private CouponHistoryService couponHistoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponUsageHistResponseDto>>> getAllUsageHistory() {

        List<CouponUsageHistResponseDto> histories =
                couponHistoryService.getAllCouponUsageHistory();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupon usage history fetched successfully",
                        histories
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponUsageHistResponseDto>> getUsageHistoryById(
            @PathVariable Long id) {

        CouponUsageHistResponseDto history =
                couponHistoryService.getCouponUsageHistoryById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupon usage history fetched successfully",
                        history
                )
        );
    }

    @GetMapping("/by-ids")
    public ResponseEntity<ApiResponse<List<CouponUsageHistResponseDto>>> getUsageHistoryListById(
            @RequestParam List<Long> ids) {

        List<CouponUsageHistResponseDto> historyList =
                couponHistoryService.getCouponUsageHistoryListById(ids);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupon usage history list fetched successfully",
                        historyList
                )
        );
    }
}