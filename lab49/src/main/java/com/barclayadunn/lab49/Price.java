package com.barclayadunn.lab49;

import java.math.BigDecimal;

/**
 * User: barclayadunn
 * Date: 6/12/15
 * Time: 5:03 PM
 */
public class Price {

    private String productName;
    private BigDecimal price;

    public Price(String productName, BigDecimal price) {
        this.price = price;
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }
}
