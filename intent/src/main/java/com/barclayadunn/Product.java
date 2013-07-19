package com.barclayadunn;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 3:17 PM
 */
public class Product {

    private String productCode;
    private double singlePrice; // price per item when buying fewer than lotCount
    private int lotCount;       // number of items to get lotPrice
    private double lotPrice;    // price per lotCount items

    Product(String productCode, double singlePrice, int lotCount, double lotPrice) {
        this.productCode = productCode;
        this.singlePrice = singlePrice;
        this.lotCount = lotCount;
        this.lotPrice = lotPrice;
    }

    public String getProductCode() { return productCode; }

    public double getSinglePrice() { return singlePrice; }

    public boolean hasLotPricing() {
        return lotCount != 0;
    }

    public double getPriceForCount(int totalCount) {
        if (hasLotPricing()) {
            int nonLotCount = totalCount % lotCount;
            int lots = (totalCount - nonLotCount) / lotCount;
            return lots * lotPrice + nonLotCount * singlePrice;
        } else {
            return totalCount * singlePrice;
        }
    }
}
