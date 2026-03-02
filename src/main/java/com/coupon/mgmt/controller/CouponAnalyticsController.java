package com.coupon.mgmt.controller;

import com.coupon.mgmt.dtos.response.ApiResponse;
import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.service.CouponAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons/analytics")
@RequiredArgsConstructor
public class CouponAnalyticsController {

    private final CouponAnalyticsService couponAnalyticsService;

    @GetMapping("/top-performing")
    public ResponseEntity<ApiResponse<List<Coupon>>> getTopPerformingCoupons(
            @RequestParam(defaultValue = "10") int limit) {

        List<Coupon> coupons =
                couponAnalyticsService.getTopPerformingCoupons(limit);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Top performing coupons fetched successfully",
                        coupons
                )
        );
    }

    @GetMapping("/least-used")
    public ResponseEntity<ApiResponse<List<Coupon>>> getLeastUsedCoupons(
            @RequestParam(defaultValue = "10") int limit) {

        List<Coupon> coupons =
                couponAnalyticsService.getLeastUsedCoupons(limit);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Least used coupons fetched successfully",
                        coupons
                )
        );
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<ApiResponse<List<Coupon>>> getCouponsExpiringSoon(
            @RequestParam(defaultValue = "7") int days) {

        List<Coupon> coupons =
                couponAnalyticsService.getCouponsExpiringSoon(days);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupons expiring soon fetched successfully",
                        coupons
                )
        );
    }
}