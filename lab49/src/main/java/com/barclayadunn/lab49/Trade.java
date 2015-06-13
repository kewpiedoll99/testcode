package com.barclayadunn.lab49;

import java.math.BigDecimal;

/**
 * User: barclayadunn
 * Date: 6/12/15
 * Time: 5:02 PM
 */
public class Trade {

    private String productName;
    private Direction direction;
    private BigDecimal price;
    private int quantity;

    public Trade(String productName, Direction direction, BigDecimal price, int quantity) {
        this.direction = direction;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Direction getDirection() {
        return direction;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    enum Direction { BUY, SELL }
}
