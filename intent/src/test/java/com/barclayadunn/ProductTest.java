package com.barclayadunn;

import org.junit.Test;

import java.math.BigDecimal;

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
    public void testThresholdProductPrices() {
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
        productCountG = codeTestMaxCountG;
    }

    private void givenOneOfEachProduct() {
        productCountA = productCountB = productCountC = productCountD = productCountG = 1;
    }

    private void givenLotNumberOfEachProduct() {
        productCountA = lotCountA;
        productCountB = lotCountB;
        productCountC = lotCountC;
        productCountD = lotCountD;
        productCountG = lotCountG;
    }

    private void thenProductsExistWithCorrectProductCodes() {
        assertEquals("A", productA.getProductCode());
        assertEquals("B", productB.getProductCode());
        assertEquals("C", productC.getProductCode());
        assertEquals("D", productD.getProductCode());
        assertEquals("G", productG.getProductCode());
    }

    private void thenValidSinglePricesAreDefined() {
        assertEquals(0, singlePriceA.compareTo(productA.getPriceForCount(productCountA)));
        assertEquals(0, singlePriceA.compareTo(productA.getSinglePrice()));
        assertEquals(0, singlePriceB.compareTo(productB.getPriceForCount(productCountB)));
        assertEquals(0, singlePriceB.compareTo(productB.getSinglePrice()));
        assertEquals(0, singlePriceC.compareTo(productC.getPriceForCount(productCountC)));
        assertEquals(0, singlePriceC.compareTo(productC.getSinglePrice()));
        assertEquals(0, singlePriceD.compareTo(productD.getPriceForCount(productCountD)));
        assertEquals(0, singlePriceD.compareTo(productD.getSinglePrice()));
        assertEquals(0, singlePriceG.compareTo(productG.getPriceForCount(productCountG)));
        assertEquals(0, singlePriceG.compareTo(productG.getSinglePrice()));
    }

    private void thenValidLotPricesAreDefined() {
        assertTrue(productA.hasDiscountedPricing());
        assertEquals(0, lotPriceA.compareTo(productA.getPriceForCount(productCountA)));
        assertFalse(productB.hasDiscountedPricing());
        assertEquals(0, singlePriceB.multiply(new BigDecimal(productCountB)).compareTo(productB.getPriceForCount(productCountB)));
        assertTrue(productC.hasDiscountedPricing());
        assertEquals(0, lotPriceC.compareTo(productC.getPriceForCount(productCountC)));
        assertFalse(productD.hasDiscountedPricing());
        assertEquals(0, singlePriceD.multiply(new BigDecimal(productCountD)).compareTo(productD.getPriceForCount(productCountD)));
        assertTrue(productG.hasDiscountedPricing());
        assertEquals(0, singlePriceG.multiply(new BigDecimal(productCountG)).compareTo(productG.getPriceForCount(productCountG)));
    }

    private void thenValidCodeTestPricesAreDefined() {
        assertEquals(0, codeTestTotalA.compareTo(productA.getPriceForCount(productCountA)));
        assertEquals(0, codeTestTotalB.compareTo(productB.getPriceForCount(productCountB)));
        assertEquals(0, codeTestTotalC.compareTo(productC.getPriceForCount(productCountC)));
        assertEquals(0, codeTestTotalD.compareTo(productD.getPriceForCount(productCountD)));
        assertEquals(0, codeTestTotalG.compareTo(productG.getPriceForCount(productCountG)));
    }
}
