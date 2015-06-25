package com.barclayadunn.lab49;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * User: barclayadunn
 * Date: 6/12/15
 * Time: 5:09 PM
 */
public class TestAutomatedTrader {

    String [] productNames = new String[]{"XON", "AAPL", "BERK", "BP", "RDSA"};

    AutomatedTrader automatedTrader;

    Trade tradeOutput;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        automatedTrader = new AutomatedTrader(productNames);
    }

    @Test
    public void testBuildTrades() {
        BiFunction<String, String, Price> createPrice = (a, b) -> new Price(a, new BigDecimal(b));
        Function<Price, Trade> buildTrade = automatedTrader::buildTrades;

        tradeOutput = buildTrade.apply(createPrice.apply("BP","7.61"));
        assertNull(tradeOutput);
        tradeOutput = buildTrade.apply(createPrice.apply("RDSA","2201.00"));
        assertNull(tradeOutput);
        tradeOutput = buildTrade.apply(createPrice.apply("RDSA","2209.00"));
        assertNull(tradeOutput);
        tradeOutput = buildTrade.apply(createPrice.apply("BP","7.66"));
        assertNull(tradeOutput);
        tradeOutput = buildTrade.apply(createPrice.apply("BP","7.64"));
        assertNull(tradeOutput);

        tradeOutput = buildTrade.apply(createPrice.apply("BP","7.67"));
        assertNotNull(tradeOutput);
        assertEquals("BP", tradeOutput.getProductName());
        assertEquals(Trade.Direction.BUY, tradeOutput.getDirection());
        assertTrue(new BigDecimal("7.67").compareTo(tradeOutput.getPrice()) == 0);
        assertEquals(1000, tradeOutput.getQuantity());

        tradeOutput = buildTrade.apply(createPrice.apply("RDSA","2101.00"));
        assertNull(tradeOutput);
        tradeOutput = buildTrade.apply(createPrice.apply("RDSA","2109.00"));
        assertNotNull(tradeOutput);
        assertEquals("RDSA", tradeOutput.getProductName());
        assertEquals(Trade.Direction.SELL, tradeOutput.getDirection());
        assertTrue(new BigDecimal("2109.00").compareTo(tradeOutput.getPrice()) == 0);
        assertEquals(1000, tradeOutput.getQuantity());
    }

    @Test
    public void testAverageLastFourPrices() {
        List<BigDecimal> testPriceList = new ArrayList<>();
        for (int i = 1; i < AutomatedTrader.PRICE_AVG_COUNT + 1; i++) {
            testPriceList.add(new BigDecimal("" + i + ".00"));
        }
        BigDecimal average = automatedTrader.averageLastFourPrices(testPriceList);
        assertTrue(average.compareTo(new BigDecimal("2.50")) == 0);

        testPriceList = new ArrayList<>();
        for (int i = 1; i < AutomatedTrader.PRICE_AVG_COUNT + 1; i++) {
            testPriceList.add(new BigDecimal("-" + i + ".00"));
        }
        average = automatedTrader.averageLastFourPrices(testPriceList);
        assertTrue(average.compareTo(new BigDecimal("-2.50")) == 0);
    }
}
