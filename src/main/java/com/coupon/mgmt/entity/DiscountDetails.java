package com.coupon.mgmt.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Embeddable
@Data
public class DiscountDetails {
    // Cart-wise discount details
    private Double threshold;
    // Cart/product-wise discount percentage
    private Double discountPercentage;

    // Product-wise discount details
    private Integer productId;

    @ElementCollection // BxGy buy products
    private List<ProductQuantity> buyProducts;

    @ElementCollection   // BxGy get products
    private List<ProductQuantity> getProducts;

    private Double maxDiscountAmount;
}
