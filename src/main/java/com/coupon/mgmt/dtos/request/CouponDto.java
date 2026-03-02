package com.coupon.mgmt.dtos.request;

import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.entity.DiscountDetails;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {
    private String type;
    private String code;

    @Embedded
    private DiscountDetails details;

    private int validationDays;

    // For BxGy type
    private int repetitionLimit;
    private Integer maxUsagePerUser;

    public static Coupon toEntity(CouponDto dto) {
        if (dto == null) {
            return null;
        }

        Coupon coupon = new Coupon();
        coupon.setCode(dto.getCode());
        coupon.setType(CouponType.valueOf(dto.getType()));
        coupon.setDetails(dto.getDetails());
        int validationDay= dto.getValidationDays();
        if (Objects.isNull(validationDay) || validationDay<=0){
            validationDay= 7;
        }
        coupon.setExpirationDate(System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(validationDay));
        coupon.setRepetitionLimit(dto.getRepetitionLimit());
        coupon.setMaxUsagePerUser(dto.getMaxUsagePerUser());
        return coupon;
    }



    public static CouponDto toDto(Coupon entity) {
        if (entity == null) {
            return null;
        }

        CouponDto couponDto = new CouponDto();
        couponDto.setType(entity.getType().name());
        couponDto.setDetails(entity.getDetails());
        couponDto.setRepetitionLimit(entity.getRepetitionLimit());
        return couponDto;
    }
}