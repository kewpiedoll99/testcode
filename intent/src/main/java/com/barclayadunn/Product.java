package com.barclayadunn;

import java.math.BigDecimal;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 3:17 PM
 */
public class Product {

    private String productCode;
    private Discount discount;

    Product(String productCode1, Discount discount1) {
        productCode = productCode1;
        discount = discount1;
    }

    public String getProductCode() {
        return productCode;
    }

    public BigDecimal getSinglePrice() {
        return discount.getSinglePrice();
    }

    public boolean hasDiscountedPricing() {
        return discount.getActivatorCount() != 0;
    }

    public BigDecimal getPriceForCount(int totalCount) {
        return discount.getPriceForCount(totalCount);
    }
}

abstract class Discount {

    BigDecimal singlePrice; // normal price per item
    BigDecimal discountPrice;
    int activatorCount; // number of items to get discount

    Discount(BigDecimal singlePrice1, BigDecimal discountPrice1, int activatorCount1) {
        singlePrice = singlePrice1;
        discountPrice = discountPrice1;
        activatorCount = activatorCount1;
    }

    abstract BigDecimal getPriceForCount(int totalCount);

    public BigDecimal getSinglePrice() { return singlePrice; }
    public int getActivatorCount() { return activatorCount; }
}

class NoDiscount extends Discount {

    NoDiscount(BigDecimal singlePrice, BigDecimal discountPrice, int activatorCount) {
        super(singlePrice, discountPrice, activatorCount);
    }

    BigDecimal getPriceForCount(int totalCount) {
        return singlePrice.multiply(new BigDecimal(totalCount));
    }
}

class LotDiscount extends Discount {

    LotDiscount(BigDecimal singlePrice, BigDecimal discountPrice, int activatorCount) {
        super(singlePrice, discountPrice, activatorCount);
    }

    BigDecimal getPriceForCount(int totalCount) {
        int nonLotCount = totalCount % activatorCount;
        int lots = (totalCount - nonLotCount) / activatorCount;
        return discountPrice.multiply(new BigDecimal(lots)).add(singlePrice.multiply(new BigDecimal(nonLotCount)));
    }
}

class ThresholdDiscount extends Discount {

    ThresholdDiscount(BigDecimal singlePrice, BigDecimal discountPrice, int activatorCount) {
        super(singlePrice, discountPrice, activatorCount);
    }

    BigDecimal getPriceForCount(int totalCount) {
        if (totalCount > activatorCount)
            return discountPrice.multiply(new BigDecimal(totalCount));
        else
            return singlePrice.multiply(new BigDecimal(totalCount));
    }
}
