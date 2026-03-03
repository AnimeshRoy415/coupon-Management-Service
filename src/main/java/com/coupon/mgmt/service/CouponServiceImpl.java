package com.coupon.mgmt.service;

import com.coupon.mgmt.dtos.request.CouponDto;
import com.coupon.mgmt.dtos.response.CouponResponseDto;
import com.coupon.mgmt.entity.Coupon;
import com.coupon.mgmt.entity.CouponType;
import com.coupon.mgmt.entity.CouponUsageHistory;
import com.coupon.mgmt.entity.ProductQuantity;
import com.coupon.mgmt.exception.ConditionNotMeet;
import com.coupon.mgmt.exception.CouponExpire;
import com.coupon.mgmt.exception.CouponNotFound;
import com.coupon.mgmt.exception.FraudDetectedException;
import com.coupon.mgmt.fraud.model.FraudScore;
import com.coupon.mgmt.fraud.service.FraudDetectionService;
import com.coupon.mgmt.model.Cart;
import com.coupon.mgmt.model.CartItem;
import com.coupon.mgmt.repository.CouponRepository;
import com.coupon.mgmt.repository.CouponUsageHistoryRepository;
import com.coupon.mgmt.strategy.DiscountStrategy;

import com.coupon.mgmt.validation.BxGyValidator;
import com.coupon.mgmt.validation.CouponValidatorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final Map<CouponType, DiscountStrategy> strategyMap;

    public CouponServiceImpl(List<DiscountStrategy> strategies) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        DiscountStrategy::getType,
                        s -> s
                ));
    }
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUsageHistoryRepository couponUsageHistoryRepository;

    @Autowired
    private BxGyValidator bxGyValidator;

    @Autowired
    private CouponValidatorFactory validatorFactory;

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private UserBlockedCouponService blockedCouponService;


    @Override
    public Coupon createCoupon(CouponDto couponDto) {

        CouponType type = CouponType.valueOf(couponDto.getType());
        validatorFactory.getValidator(type).validate(couponDto);
        Coupon coupon = CouponDto.toEntity(couponDto);
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon getCouponById(Long id) {

        Optional<Coupon> optionalCoupon = couponRepository.findById(id);
        if (Objects.isNull(optionalCoupon)) {
            throw new CouponNotFound("Coupon not found");
        }
        return optionalCoupon.get();
    }

    @Override
    public List<Coupon> getAllCoupons(Boolean isActive) {
        if (Objects.isNull(isActive)) {
            return couponRepository.findAll();
        } else if (isActive) {
            return couponRepository.findAllActiveCoupon(System.currentTimeMillis());
        } else {
            return couponRepository.findAllDeActiveCoupon(System.currentTimeMillis());
        }
    }

    @Override
    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        return couponRepository.findById(id)

                .map(coupon -> {
                    coupon.setType(updatedCoupon.getType());
                    coupon.setDetails(updatedCoupon.getDetails());
//                    coupon.setExpirationDate(updatedCoupon.getExpirationDate());
                    coupon.setRepetitionLimit(updatedCoupon.getRepetitionLimit());
                    return couponRepository.save(coupon);
                })
                .orElseThrow(() -> new CouponNotFound("Coupon not found"));
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public List<CouponResponseDto> findApplicableCoupons(Cart cart) {

        List<CouponResponseDto> applicableCoupons = new ArrayList<>();

        List<Coupon> allCoupons =
                couponRepository.findAllActiveCoupon(System.currentTimeMillis());

        for (Coupon coupon : allCoupons) {

            DiscountStrategy strategy =
                    strategyMap.get(coupon.getType());

            if (strategy == null) {
                continue;
            }

            double discount =
                    strategy.applyDiscount(coupon, cart);

            if (discount > 0) {

                CouponResponseDto dto = new CouponResponseDto();
                dto.setId(coupon.getId());
                dto.setType(coupon.getType());
                dto.setDiscount(discount);

                applicableCoupons.add(dto);
            }
        }

        return applicableCoupons;
    }

    private void validateUsageLimit(Coupon coupon, Long userId) {

        if (coupon.getMaxUsagePerUser() == null) {
            return; // unlimited
        }

        long usageCount = couponUsageHistoryRepository
                .countByCouponIdAndUserId(coupon.getId(), userId);

        if (usageCount >= coupon.getMaxUsagePerUser()) {
            throw new IllegalArgumentException(
                    "Coupon usage limit exceeded for this user!"
            );
        }
    }

    @Override
    @Transactional
    public Cart applyCoupon(Long couponId, Cart cart) {

        // 1️⃣ Fetch coupon
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFound("Coupon not found!"));

        // 2️⃣ Expiry check
        if (coupon.isExpired()) {
            throw new CouponExpire("Coupon Expired!");
        }

        validateUsageLimit(coupon, cart.getUserId());


        // 2️⃣ 🚨 FRAUD CHECK (Run once per request)
        FraudScore fraudScore =
                fraudDetectionService.evaluateUser(cart.getUserId());

        System.out.println(fraudScore);
        if (fraudScore.getLevel().isBlocked()) {

            System.out.println(fraudScore);
            blockedCouponService.blockCoupon(cart.getUserId(), coupon.getId(),
                    String.join(", ", fraudScore.getRiskFactors()));
            log.warn("Fraud detected for user {} | Level: {} | Risk Factors: {}",
                    cart.getUserId(),
                    fraudScore.getLevel(),
                    fraudScore.getRiskFactors());

            throw new FraudDetectedException(
                    "Suspicious activity detected. Coupons blocked."
            );
        }

        // 4️⃣ Get strategy
        DiscountStrategy strategy = strategyMap.get(coupon.getType());

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unsupported coupon type: " + coupon.getType()
            );
        }

        // 5️⃣ Apply discount
        double discount = strategy.applyDiscount(coupon, cart);

        if (discount <= 0) {
            return cart;
        }

        // 6️⃣ Update analytics
        coupon.incrementRedemptionCount();
        coupon.addToRevenue(discount);
        couponRepository.save(coupon);

        // 7️⃣ Record usage
        recordCouponUsage(cart, couponId, discount);



        return cart;
    }


    /*private double calculateBxGyDiscount(Coupon coupon, Cart cart) {

        if (coupon.getExpirationDate() < System.currentTimeMillis()) {
            throw new CouponExpire("Coupon Expire!!!");
        }
        double totalDiscount = 0;
        List<ProductQuantity> buyProducts = coupon.getDetails().getBuyProducts();
        List<ProductQuantity> getProducts = coupon.getDetails().getGetProducts();

        int repetitionLimit = coupon.getRepetitionLimit();
        int totalApplicableRepetitions = Integer.MAX_VALUE; // Initialize with a high value

        // Calculate the maximum number of times the coupon can be applied based on buy products
        for (ProductQuantity buyProduct : buyProducts) {
            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(buyProduct.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                int timesApplicable = cartItem.getQuantity() / buyProduct.getQuantity();
                totalApplicableRepetitions = Math.min(totalApplicableRepetitions, timesApplicable);
            }

        }

        totalApplicableRepetitions = Math.min(totalApplicableRepetitions, repetitionLimit);

        // Calculate discount for each get product
        for (ProductQuantity getProduct : getProducts) {
            int finalTotalApplicableRepetitions = totalApplicableRepetitions;
            CartItem freeItem = cart.getItems().stream()
                    .filter(item -> {
                        if (item.getProductId().equals(getProduct.getProductId())) {
                            item.setTotalDiscount(finalTotalApplicableRepetitions * getProduct.getQuantity() * item.getPrice());
                        }

                        return item.getProductId().equals(getProduct.getProductId());
                    })
                    .findFirst()
                    .orElse(null);

            if (freeItem != null) {
                totalDiscount += totalApplicableRepetitions * getProduct.getQuantity() * freeItem.getPrice().doubleValue();
            } else {

            }

        }

        double totalCartPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();

        cart.setTotalPrice(totalCartPrice);
        cart.setTotalDiscount(totalDiscount);
        cart.setFinalPrice(totalCartPrice - totalDiscount);
        return totalDiscount;
    }


    private double calculateCartWiseDiscountWithCap(Coupon coupon, Cart cart) {

        if (coupon.getExpirationDate() < System.currentTimeMillis()) {
            throw new CouponExpire("Coupon Expire!!!");
        }
        double totalPrice = 0;
        for (CartItem item : cart.getItems()) {
            totalPrice += item.getQuantity() * item.getPrice();
        }

        // Threshold check
        double threshold = coupon.getDetails().getThreshold();
        if (totalPrice < threshold) {
            throw new ConditionNotMeet("Minimum cart value not met!");
        }

        // Calculate raw percentage discount
        double percentage = coupon.getDetails().getDiscountPercentage();
        System.out.println("Product dis Percentage : "+ coupon.toString());

        double totalDiscount = totalPrice * (percentage / 100);

        // Apply max discount cap (NEW LOGIC)
        Double maxCap = coupon.getDetails().getMaxDiscountAmount();
        if (maxCap != null) {
            totalDiscount = Math.min(totalDiscount, maxCap);
        }

//        Distribute discount proportionally to items
        for (CartItem item : cart.getItems()) {
            double itemTotal = item.getQuantity() * item.getPrice();
            double itemDiscount = (itemTotal / totalPrice) * totalDiscount;
            item.setTotalDiscount(itemDiscount);
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscount(totalDiscount);
        cart.setFinalPrice(totalPrice - totalDiscount);

        return totalDiscount;
    }

    private double calculateProductWiseDiscount(Coupon coupon, Cart cart) {
        double discount = 0;

        if (coupon.getExpirationDate() < System.currentTimeMillis()) {
            throw new CouponExpire("Coupon Expire!!!");
        }

        System.out.println("Cart : "+ cart);
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(coupon.getDetails().getProductId())) {

                discount += item.getPrice() * item.getQuantity() * (coupon.getDetails().getDiscountPercentage() / 100);
                item.setTotalDiscount(discount);
                System.out.println("discount  ::  "+ discount);
            }
        }
        if (discount <= 0) {
            throw new ConditionNotMeet("Condition not meet");
        }

        double totalCartPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        cart.setTotalPrice(totalCartPrice);
        cart.setTotalDiscount(discount);
        cart.setFinalPrice(totalCartPrice - discount);
        return discount;
    }*/

    /*@Override
    public Cart applyMultipleCoupons(List<Long> couponIds, Cart cart) {

        List<Coupon> couponList = couponRepository.findAllById(couponIds);
        for (Coupon coupon : couponList) {

//            Optional<Coupon> optCoupon = couponRepository.findById(couponId);
//            if (optCoupon.isEmpty()) {
//                throw new CouponNotFound("Coupon not found!!!");
//            }
//            Coupon coupon = optCoupon.get();

            if (coupon.getExpirationDate() < System.currentTimeMillis()) {
                throw new CouponExpire("Coupon Expire!!!");
            }

            double discount = 0;

            switch (coupon.getType()) {
                case BX_GY:
                    discount = calculateBxGyDiscount(coupon, cart);
                    break;
                case CART_WISE:
                    discount = calculateCartWiseDiscountWithCap(coupon, cart);
                    break;
                case PRODUCT_WISE:
                    discount = calculateProductWiseDiscount(coupon, cart);
                    break;
            }

            if (discount > 0) {
                cart.applyCoupon(coupon, discount);
            }
            // Update analytics
            coupon.incrementRedemptionCount();
            coupon.addToRevenue(cart.getFinalPrice());

//            // Record coupon usage
            recordCouponUsage(cart, coupon.getId(), discount);
        }

        couponRepository.saveAll(couponList);
        return cart;
    }*/

    @Override
    @Transactional
    public Cart applyMultipleCoupons(List<Long> couponIds, Cart cart) {

        // 1️⃣ Basic Validation
        if (cart == null || cart.getUserId() == null) {
            throw new IllegalArgumentException("Invalid cart or user information");
        }

        if (couponIds == null || couponIds.isEmpty()) {
            return cart;
        }
        // 3️⃣ Fetch Coupons
        List<Coupon> coupons = couponRepository.findAllById(couponIds);

        if (coupons.isEmpty()) {
            throw new CouponNotFound("No coupons found for given IDs");
        }

        // 4️⃣ Ensure appliedCoupons list initialized ONCE
        if (cart.getAppliedCoupons() == null) {
            cart.setAppliedCoupons(new ArrayList<>());
        }

        FraudScore fraudScore = fraudDetectionService.evaluateUser(cart.getUserId());
        System.out.println("ertyuiuyt    "+fraudScore.getScore());
        System.out.println("fffffffffffffff   "+fraudScore.getUserId());
        System.out.println(fraudScore.getRiskFactors());
        if (fraudScore.getLevel().isBlocked()) {
//            System.out.println(fraudScore.ge
//            erId());
            // Optionally save blocked coupons
            for (Coupon coupon : coupons) {
                blockedCouponService.blockCoupon(
                        cart.getUserId(),
                        coupon.getId(),
                        String.join(", ", fraudScore.getRiskFactors())
                );
            }

            log.warn("Fraud detected for user {} | Level: {} | Risk Factors: {}",
                    cart.getUserId(),
                    fraudScore.getLevel(),
                    fraudScore.getRiskFactors());

            throw new FraudDetectedException("Suspicious activity detected. Coupons blocked.");
        }

        // 5️⃣ Apply Coupons Sequentially
        for (Coupon coupon : coupons) {

            if (blockedCouponService.isBlocked(cart.getUserId(), coupon.getId())) {
                log.info("Coupon {} is currently blocked for user {}, skipping.", coupon.getCode(), cart.getUserId());
                continue;
            }
            // Expiry Check
            if (coupon.isExpired()) {
                throw new CouponExpire("Coupon Expired: " + coupon.getCode());
            }

            // Usage Limit Check
            validateUsageLimit(coupon, cart.getUserId());

            System.out.println("coupon type : "+ coupon.getType());
            // Strategy Lookup
            DiscountStrategy strategy = strategyMap.get(coupon.getType());

            System.out.println(strategyMap.size());

            if (strategy == null) {
                log.error("Unsupported coupon type : {}", coupon.getType());
                throw new IllegalArgumentException(
                        "Unsupported coupon type: " + coupon.getType()
                );
            }

            // Apply Discount
            double discount = strategy.applyDiscount(coupon, cart);

            if (discount <= 0) {
                log.info("Coupon {} not applicable for user {}",
                        coupon.getCode(),
                        cart.getUserId());
//                continue;
            }

            // Add to Cart
            cart.applyCoupon(coupon, discount);

            // Update Analytics
            coupon.incrementRedemptionCount();
            coupon.addToRevenue(discount);

            // Record Usage History
            recordCouponUsage(cart, coupon.getId(), discount);

            log.info("Coupon {} applied successfully for user {} with discount {}",
                    coupon.getCode(),
                    cart.getUserId(),
                    discount);


        }

        // 6️⃣ Persist analytics updates
        couponRepository.saveAll(coupons);

        return cart;
    }

    @Override
    public void recordCouponUsage(Cart cart, Long couponId, Double discountApplied) {
        Coupon coupon = getCouponById(couponId);

        ObjectMapper objectMapper = new ObjectMapper();

        String cartJson;
        try {
            cartJson = objectMapper.writeValueAsString(cart);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing cart to JSON", e);
        }
            CouponUsageHistory usageHistory = new CouponUsageHistory();
            usageHistory.setCoupon(coupon);
            usageHistory.setCart(cartJson);
            usageHistory.setUsedAt(LocalDateTime.now(ZoneOffset.UTC));
            usageHistory.setDiscountApplied(discountApplied);
            usageHistory.setUserId(cart.getUserId());

            System.out.println("usageHistory   :::   "+ usageHistory);
            CouponUsageHistory couponUsageHistory= couponUsageHistoryRepository.save(usageHistory);
            log.info("CouponUsageHistory is saved : {}", couponUsageHistory);
    }

}


