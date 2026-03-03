package com.coupon.mgmt.validation;

import com.coupon.mgmt.entity.CouponType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class CouponValidatorFactory {

    private final Map<CouponType, CouponValidator> validatorMap;

    public CouponValidatorFactory(List<CouponValidator> validators) {

        validatorMap = new EnumMap<>(CouponType.class);

        for (CouponValidator validator : validators) {
            validatorMap.put(validator.getType(), validator);
        }
    }

    public CouponValidator getValidator(CouponType type) {

        CouponValidator validator = validatorMap.get(type);

        if (validator == null) {
            throw new IllegalArgumentException("No validator found for type: " + type);
        }

        return validator;
    }


}
