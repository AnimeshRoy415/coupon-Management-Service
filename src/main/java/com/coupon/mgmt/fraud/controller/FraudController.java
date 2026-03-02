package com.coupon.mgmt.fraud.controller;

import com.coupon.mgmt.dtos.response.ApiResponse;
import com.coupon.mgmt.fraud.model.FraudScore;
import com.coupon.mgmt.fraud.service.FraudDetectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fraud")
public class FraudController {

    private final FraudDetectionService service;

    public FraudController(FraudDetectionService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<FraudScore>> evaluateUser(
            @PathVariable Long userId) {

        FraudScore score = service.evaluateUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Fraud evaluation complete",
                        score
                )
        );
    }
}