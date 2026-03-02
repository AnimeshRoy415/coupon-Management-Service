package com.coupon.mgmt.strategy;

import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.exception.ConditionNotMeet;
import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class ProductWiseStrategy implements DiscountStrategy {

    @Override
    public CouponType getType() {
        return CouponType.PRODUCT_WISE;
    }

    @Override
    public double applyDiscount(Coupon coupon, Cart cart) {

        double totalDiscount = 0.0;

        // 1️⃣ Expiry Check
        if (coupon.isExpired()) {
            throw new RuntimeException("Coupon Expired!");
        }

        Integer productId = coupon.getDetails().getProductId();
        Double percentage = coupon.getDetails().getDiscountPercentage();

        if (productId == null || percentage == null) {
            throw new ConditionNotMeet("Invalid Product Wise Coupon Configuration");
        }

        // 2️⃣ Apply discount only on matching product
        for (CartItem item : cart.getItems()) {
            System.out.println("cart.getItems ::  "+item);

            if (item.getProductId().equals(productId)) {

                double itemTotal = item.getPrice() * item.getQuantity();
                double discount = itemTotal * (percentage / 100);

                System.out.println("discount  :: "+ discount);
                item.setTotalDiscount(discount);
                totalDiscount += discount;
            }
        }

//        if (totalDiscount <= 0) {
//            throw new ConditionNotMeet("Product not found in cart");
//        }

        // 3️⃣ Update cart totals
        double totalPrice = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscount(totalDiscount);
        cart.setFinalPrice(totalPrice - totalDiscount);

        return totalDiscount;
    }
}