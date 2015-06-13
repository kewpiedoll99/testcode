package com.barclayadunn.lab49;

/**
 * User: barclayadunn
 * From assignment provided by Lab49
 * Date: 6/12/15
 * Time: 5:01 PM
 */
public interface TradingAlgorithm {

    /**
    * Builds a trade to be executed based on the supplied prices.
    * @param price data
    * @return trade to execute
    */
    Trade buildTrades(Price price);
}
