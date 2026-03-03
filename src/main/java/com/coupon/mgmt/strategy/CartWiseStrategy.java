package com.coupon.mgmt.strategy;


import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.exception.ConditionNotMeet;
import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartWiseStrategy implements DiscountStrategy{

    @Override
    public CouponType getType() {
        return CouponType.CART_WISE;
    }

    @Override
    public double applyDiscount(Coupon coupon, Cart cart) {

        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        cart.setTotalPrice(totalPrice);

        Double threshold = coupon.getDetails().getThreshold();
        if (threshold != null && totalPrice < threshold) {
//            throw new ConditionNotMeet("Cart value below required threshold!");
            return 0.0;
        }

        // Calculate raw percentage discount
        double percentage = coupon.getDetails().getDiscountPercentage();
        double discount = totalPrice * (percentage / 100);
        // Apply max discount cap if present
        Double maxCap = coupon.getDetails().getMaxDiscountAmount();
        if (maxCap != null && discount > maxCap) {
            discount = maxCap;
        }

        // Distribute discount proportionally across items
        if (totalPrice > 0) {
            for (CartItem item : cart.getItems()) {

                double itemTotal = item.getPrice() * item.getQuantity();
                double itemDiscount = (itemTotal / totalPrice) * discount;

                item.setTotalDiscount(itemDiscount);
            }
        }

        // Update cart totals
        cart.setTotalDiscount(discount);
        cart.setFinalPrice(totalPrice - discount);

        return discount;
    }
}
