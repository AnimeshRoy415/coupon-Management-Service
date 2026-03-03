package com.coupon.mgmt.validation;

import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.entity.DiscountDetails;
import org.springframework.stereotype.Component;

@Component
public class ProductWiseValidator implements  CouponValidator{

    @Override
    public CouponType getType() {
        return CouponType.PRODUCT_WISE;
    }

    @Override
    public void validate(CouponDto dto) {

        DiscountDetails details = dto.getDetails();

        if (details.getProductId() == null) {
            throw new IllegalArgumentException("ProductId is required");
        }

        if (details.getDiscountPercentage() == null ||
                details.getDiscountPercentage() <= 0 ||
                details.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount % must be between 1 and 100");
        }
    }
}
