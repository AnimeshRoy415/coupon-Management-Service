package com.coupon.mgmt.controller;


import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.dtos.request.ApplyCouponsRequest;
import com.coupon.mgmt.dtos.response.ApiResponse;
import com.coupon.mgmt.dtos.response.CouponResponseDto;
import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Validated
public class CouponController {

    private final CouponService couponService;

    // ========================= CREATE =========================

    @PostMapping
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(
            @RequestBody CouponDto request) {

        Coupon response =
                couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED,
                        "Coupon created successfully",
                        response
                ));
    }

    // ========================= GET BY ID =========================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Coupon>> getCouponById(
            @PathVariable Long id) {

        Coupon response =
                couponService.getCouponById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupon fetched successfully",
                        response
                )
        );
    }

    // ========================= GET ALL =========================

    @GetMapping
    public ResponseEntity<ApiResponse<List<Coupon>>> getAllCoupons(
            @RequestParam(required = false) Boolean active) {

        List<Coupon> response =
                couponService.getAllCoupons(active);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupons fetched successfully",
                        response
                )
        );
    }

    // ========================= UPDATE =========================

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Coupon>> updateCoupon(
            @PathVariable Long id,
            @RequestBody Coupon request) {

        Coupon response =
                couponService.updateCoupon(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupon updated successfully",
                        response
                )
        );
    }

    // ========================= DELETE =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(
            @PathVariable Long id) {

        couponService.deleteCoupon(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupon deleted successfully",
                        null
                )
        );
    }

    // ========================= FIND APPLICABLE =========================

    @PostMapping("/applicable")
    public ResponseEntity<ApiResponse<List<CouponResponseDto>>> getApplicableCoupons(
            @RequestBody Cart cart) {

        List<CouponResponseDto> response =
                couponService.findApplicableCoupons(cart);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Applicable coupons fetched successfully",
                        response
                )
        );
    }

    // ========================= APPLY MULTIPLE =========================

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<Cart>> applyMultipleCoupons(
            @RequestBody ApplyCouponsRequest request) {

        Cart updatedCart =
                couponService.applyMultipleCoupons(
                        request.getCouponIds(),
                        request.getCart()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Coupons applied successfully",
                        updatedCart
                )
        );
    }

}