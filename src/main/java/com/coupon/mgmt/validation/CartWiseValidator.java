package com.coupon.mgmt.validation;

import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.entity.DiscountDetails;
import org.springframework.stereotype.Component;

@Component
public class CartWiseValidator implements CouponValidator{

    @Override
    public CouponType getType() {
        return CouponType.CART_WISE;
    }

    @Override
    public void validate(CouponDto dto) {

        System.out.println("cartcart");
        DiscountDetails details = dto.getDetails();

        if (details.getThreshold() == null || details.getThreshold() <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }

        if (details.getDiscountPercentage() == null ||
                details.getDiscountPercentage() <= 0 ||
                details.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount % must be between 1 and 100");
        }

        if (details.getMaxDiscountAmount() != null &&
                details.getMaxDiscountAmount() <= 0) {
            throw new IllegalArgumentException("Max discount amount must be positive");
        }
    }
}
