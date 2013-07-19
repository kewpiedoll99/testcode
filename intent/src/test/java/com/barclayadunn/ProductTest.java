package com.barclayadunn;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 3:43 PM
 */
public class ProductTest extends TestData {

    @Test
    public void testSingleProductPrices() {
        givenProductSet();
        givenOneOfEachProduct();
        thenProductsExistWithCorrectProductCodes();
        thenValidSinglePricesAreDefined();
    }

    @Test
    public void testLotProductPrices() {
        givenProductSet();
        givenLotNumberOfEachProduct();
        thenProductsExistWithCorrectProductCodes();
        thenValidLotPricesAreDefined();
    }

    @Test
    public void testMultipleProductPrices() {
        givenProductSet();
        givenCodeTestNumberOfEachProduct();
        thenProductsExistWithCorrectProductCodes();
        thenValidCodeTestPricesAreDefined();
    }

    private void givenCodeTestNumberOfEachProduct() {
        productCountA = codeTestMaxCountA;
        productCountB = codeTestMaxCountB;
        productCountC = codeTestMaxCountC;
        productCountD = codeTestMaxCountD;
    }

    private void givenOneOfEachProduct() {
        productCountA = productCountB = productCountC = productCountD = 1;
    }

    private void givenLotNumberOfEachProduct() {
        productCountA = lotCountA;
        productCountB = lotCountB;
        productCountC = lotCountC;
        productCountD = lotCountD;
    }

    private void thenProductsExistWithCorrectProductCodes() {
        assertEquals("A", productA.getProductCode());
        assertEquals("B", productB.getProductCode());
        assertEquals("C", productC.getProductCode());
        assertEquals("D", productD.getProductCode());
    }

    private void thenValidSinglePricesAreDefined() {
        assertEquals((Double) productA.getPriceForCount(productCountA), (Double) singlePriceA);
        assertEquals((Double) productA.getSinglePrice(), (Double) singlePriceA);
        assertEquals((Double) productB.getPriceForCount(productCountB), (Double) singlePriceB);
        assertEquals((Double) productB.getSinglePrice(), (Double) singlePriceB);
        assertEquals((Double) productC.getPriceForCount(productCountC), (Double) singlePriceC);
        assertEquals((Double) productC.getSinglePrice(), (Double) singlePriceC);
        assertEquals((Double) productD.getPriceForCount(productCountD), (Double) singlePriceD);
        assertEquals((Double) productD.getSinglePrice(), (Double) singlePriceD);
    }

    private void thenValidLotPricesAreDefined() {
        assertEquals((Double) productA.getPriceForCount(productCountA), (Double) lotPriceA);
        assertFalse(productB.hasLotPricing());
        assertEquals((Double) productB.getPriceForCount(productCountB), (Double) (singlePriceB * productCountB));
        assertEquals((Double) productC.getPriceForCount(productCountC), (Double) lotPriceC);
        assertFalse(productD.hasLotPricing());
        assertEquals((Double) productD.getPriceForCount(productCountD), (Double) (singlePriceD * productCountD));
    }

    private void thenValidCodeTestPricesAreDefined() {
        assertEquals((Double) productA.getPriceForCount(productCountA), (Double) codeTestTotalA);
        assertEquals((Double) productB.getPriceForCount(productCountB), (Double) codeTestTotalB);
        assertEquals((Double) productC.getPriceForCount(productCountC), (Double) codeTestTotalC);
        assertEquals((Double) productD.getPriceForCount(productCountD), (Double) codeTestTotalD);
    }
}
