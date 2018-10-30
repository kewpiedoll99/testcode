package com.barclayadunn;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 5:40 PM
 */
public abstract class TestData {

    Product A = new Product("A", new LotDiscount(new BigDecimal("2.00"), new BigDecimal("7.00"), 4));
    Product B = new Product("B", new NoDiscount(new BigDecimal("12.00"), new BigDecimal("0.00"), 0));
    Product C = new Product("C", new LotDiscount(new BigDecimal("1.25"), new BigDecimal("6.00"), 6));
    Product D = new Product("D", new NoDiscount(new BigDecimal("0.15"), new BigDecimal("0.00"), 0));
    Product G = new Product("G", new ThresholdDiscount(new BigDecimal("1.00"), new BigDecimal("0.90"), 10));

    Terminal terminal;
    Map<String, Product> productMap = new HashMap<String, Product>();
    Map<Integer, String> intentTestSets = new HashMap<Integer, String>();
    Map<Integer, BigDecimal> intentTestAnswers = new HashMap<Integer, BigDecimal>();

    Product productA = null, productB = null, productC = null, productD = null, productG = null;
    int productCountA = 0, productCountB = 0, productCountC = 0, productCountD = 0, productCountG = 0;

    BigDecimal singlePriceA = new BigDecimal("2.00"),
            singlePriceB = new BigDecimal("12.00"),
            singlePriceC = new BigDecimal("1.25"),
            singlePriceD = new BigDecimal("0.15"),
            singlePriceG = new BigDecimal("1.00");
    int lotCountA = 4, lotCountB = 0, lotCountC = 6, lotCountD = 0, lotCountG = 10;
    BigDecimal lotPriceA = new BigDecimal("7.00"),
            lotPriceB = new BigDecimal("0.00"),
            lotPriceC = new BigDecimal("6.00"),
            lotPriceD = new BigDecimal("0.00"),
            lotPriceG = new BigDecimal("0.90");
    int codeTestMaxCountA = 4, codeTestMaxCountB = 2, codeTestMaxCountC = 7, codeTestMaxCountD = 1, codeTestMaxCountG = 12;
    BigDecimal codeTestTotalA = new BigDecimal("7.00"),
            codeTestTotalB = new BigDecimal("24.00"),
            codeTestTotalC = new BigDecimal("7.25"),
            codeTestTotalD = new BigDecimal("0.15"),
            codeTestTotalG = new BigDecimal("10.80");

    void givenProductSet() {
        productA = A;
        productB = B;
        productC = C;
        productD = D;
        productG = G;
    }

    protected void givenTerminalContainsExpectedProducts() {
        terminal.setProducts(productMap);
    }

    protected void givenScannedProductsIsEmpty() {
        terminal.setProductsScanned(new HashMap<String, Integer>());
    }
}
