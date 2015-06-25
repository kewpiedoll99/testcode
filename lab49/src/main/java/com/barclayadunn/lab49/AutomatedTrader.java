package com.barclayadunn.lab49;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: barclayadunn
 * Date: 6/12/15
 * Time: 5:05 PM
 *
 * Assumptions:
 *  1. No mention in the exercise was made of what to do if there are fewer than four prices in memory for a product.
 *     Based on the Expected Output grid, I assume no output is expected.
 *  2. No mention on when to sell but I assume if there are four or more prices in memory for a product,
 *     return sell direction.
 */
public class AutomatedTrader implements TradingAlgorithm {

    protected static final int PRICE_AVG_COUNT = 4;
    protected static final int SCALE = 2;

    ProductPricesStore productPricesStore;

    public AutomatedTrader(String[] productNames) {
        productPricesStore = ProductPricesStore.getInstance(productNames);
    }

    @Override
    /**
     * Returns a buy trade for a quantity of 1000 at the newest price,
     * if the simple average of the last 4 prices is greater than the oldest price
     * in that collection of 4 prices, e.g. {1,2,3,4} will result in a trade,
     * as will {4,5,6,4}, but {9,2,1,4} will not.
     *
     * In other words a trade will be made when the simple average
     * of a moving window of prices has an upward trend.
     */
    public Trade buildTrades(Price price) {
        BigDecimal pricePrice = price.getPrice();
        String productName = price.getProductName();
        if (!productPricesStore.hasProductName(productName)) {
            return null;
        }

        List<BigDecimal> productPriceList;
        Map<String, List<BigDecimal>> productPriceListMapCopy = productPricesStore.getProductPriceListMap();
        if (productPriceListMapCopy.containsKey(productName)) {
            productPriceList = productPriceListMapCopy.get(productName);
        } else {
            productPriceList = new ArrayList<>();
        }
        productPriceList.add(pricePrice);
        productPricesStore.putProductPriceListInMap(productName, productPriceList);

        int size = productPriceList.size();
        if (size >= PRICE_AVG_COUNT) {
            if (averageLastFourPrices(productPriceList).compareTo(productPriceList.get(size - PRICE_AVG_COUNT)) == 1) {
                return new Trade(productName, Trade.Direction.BUY, pricePrice, 1000);
            } else {
                return new Trade(productName, Trade.Direction.SELL, pricePrice, 1000);
            }
        }
        return null;
    }

    protected BigDecimal averageLastFourPrices(List<BigDecimal> productPrices) {
        BigDecimal sum = BigDecimal.ZERO;
        int size = productPrices.size();
        for (int i = PRICE_AVG_COUNT; i > 0; i--) { // count down from 4 to 1
            sum = sum.add(productPrices.get(size - i)); // add last 4 prices
        }
        return sum.divide(new BigDecimal(PRICE_AVG_COUNT), SCALE, BigDecimal.ROUND_HALF_EVEN);
    }
}
