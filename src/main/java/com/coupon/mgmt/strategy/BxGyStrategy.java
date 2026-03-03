package com.coupon.mgmt.strategy;

import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.entity.DiscountDetails;
import com.coupon.mgmt.entity.ProductQuantity;
import com.coupon.mgmt.exception.CouponExpire;
import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class BxGyStrategy implements DiscountStrategy{

    @Override
    public CouponType getType() {
        return CouponType.BX_GY;
    }

    @Override
    public double applyDiscount(Coupon coupon, Cart cart) {

        return calculateBxGyDiscount(coupon, cart);
    }


    private double calculateBxGyDiscount(Coupon coupon, Cart cart) {

        if (coupon.getExpirationDate() < System.currentTimeMillis()) {
            throw new CouponExpire("Coupon Expired!");
        }

        DiscountDetails details = coupon.getDetails();

        double totalDiscount = 0.0;

        int maxPossibleRepetition = Integer.MAX_VALUE;

        for (ProductQuantity buy : details.getBuyProducts()) {

            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(buy.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem == null) {
                return 0;
            }

            int possible = cartItem.getQuantity() / buy.getQuantity();

            maxPossibleRepetition = Math.min(maxPossibleRepetition, possible);
        }

        if (maxPossibleRepetition <= 0) {
            return 0;
        }

        int allowedRepetition = Math.min(maxPossibleRepetition, coupon.getRepetitionLimit());

        for (ProductQuantity get : details.getGetProducts()) {

            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(get.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem == null) {
                continue;
            }

            int eligibleQuantity = Math.min(
                    cartItem.getQuantity(),
                    get.getQuantity() * allowedRepetition
            );

            double discountPerUnit = cartItem.getPrice() *
                    (details.getDiscountPercentage() != null
                            ? details.getDiscountPercentage() / 100
                            : 1.0);

            double discount = eligibleQuantity * discountPerUnit;

            cartItem.setTotalDiscount(
                    cartItem.getTotalDiscount() == null
                            ? discount
                            : cartItem.getTotalDiscount() + discount
            );

            totalDiscount += discount;
        }

        if (details.getMaxDiscountAmount() != null &&
                totalDiscount > details.getMaxDiscountAmount()) {
            totalDiscount = details.getMaxDiscountAmount();
        }

        double totalPrice = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscount(totalDiscount);
        cart.setFinalPrice(totalPrice - totalDiscount);

        return totalDiscount;
    }
}
