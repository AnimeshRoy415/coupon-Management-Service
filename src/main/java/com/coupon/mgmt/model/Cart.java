package com.coupon.mgmt.model;
import com.coupon.mgmt.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Long userId;
    private List<CartItem> items;
    private Double totalPrice;
    private Double totalDiscount;
    private Double finalPrice;
    private List<Coupon> appliedCoupons;

    public void calculateTotals() {
        totalPrice = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        finalPrice = totalPrice - totalDiscount;
    }

    public void applyCoupon(Coupon coupon, double discount) {
        appliedCoupons.add(coupon);
        totalDiscount += discount;
        finalPrice = totalPrice - totalDiscount;
    }

}