package com.barclayadunn;

import java.util.HashMap;
import java.util.Map;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 5:40 PM
 */
public abstract class TestData {

    Terminal terminal;
    Map<String, Product> productMap = new HashMap<String, Product>();
    Map<Integer, String> intentTestSets = new HashMap<Integer, String>();
    Map<Integer, Double> intentTestAnswers = new HashMap<Integer, Double>();

    Product productA = null, productB = null, productC = null, productD = null;
    int productCountA = 0, productCountB = 0, productCountC = 0, productCountD = 0;

    double singlePriceA = 2.00, singlePriceB = 12.00, singlePriceC = 1.25, singlePriceD = 0.15;
    int lotCountA = 4, lotCountB = 0, lotCountC = 6, lotCountD = 0;
    double lotPriceA = 7.00, lotPriceB = 0.00, lotPriceC = 6.00, lotPriceD = 0.00;
    int codeTestMaxCountA = 4, codeTestMaxCountB = 2, codeTestMaxCountC = 7, codeTestMaxCountD = 1;
    double codeTestTotalA = 7.00, codeTestTotalB = 24.00, codeTestTotalC = 7.25, codeTestTotalD = 0.15;

    void givenProductSet() {
        productA = new Product("A", singlePriceA, lotCountA, lotPriceA);
        productB = new Product("B", singlePriceB, lotCountB, lotPriceB);
        productC = new Product("C", singlePriceC, lotCountC, lotPriceC);
        productD = new Product("D", singlePriceD, lotCountD, lotPriceD);
    }

    protected void givenTerminalContainsExpectedProducts() {
        terminal.setProducts(productMap);
    }

    protected void givenScannedProductsIsEmpty() {
        terminal.setProductsScanned(new HashMap<String, Integer>());
    }
}
