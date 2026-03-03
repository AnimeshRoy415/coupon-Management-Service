package com.coupon.mgmt.validation;

import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.entity.DiscountDetails;
import com.coupon.mgmt.entity.ProductQuantity;
import org.springframework.stereotype.Component;

@Component
public class BxGyValidator implements CouponValidator {


    @Override
    public CouponType getType() {
        return CouponType.BX_GY;
    }

    @Override
    public void validate(CouponDto dto) {

        DiscountDetails details = dto.getDetails();

        if (details.getBuyProducts() == null || details.getBuyProducts().isEmpty()) {
            throw new IllegalArgumentException("Buy products required for BX_GY");
        }

        if (details.getGetProducts() == null || details.getGetProducts().isEmpty()) {
            throw new IllegalArgumentException("Get products required for BX_GY");
        }

        for (ProductQuantity pq : details.getBuyProducts()) {
            if (pq.getProductId() == null || pq.getQuantity() == null || pq.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid buy product details");
            }
        }

        for (ProductQuantity pq : details.getGetProducts()) {
            if (pq.getProductId() == null || pq.getQuantity() == null || pq.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid get product details");
            }
        }

        if (dto.getRepetitionLimit() <= 0) {
            throw new IllegalArgumentException("Repetition limit must be greater than 0");
        }

        if (details.getDiscountPercentage() != null &&
                (details.getDiscountPercentage() <= 0 ||
                        details.getDiscountPercentage() > 100)) {
            throw new IllegalArgumentException("Discount % must be between 1 and 100");
        }
    }
}