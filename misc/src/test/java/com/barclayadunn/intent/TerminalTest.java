package com.barclayadunn.intent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.barclayadunn.intent.exception.NullProductException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 4:55 PM
 */
public class TerminalTest extends TestData {

    @Before
    public void setup() {
        terminal = new Terminal();
        givenProductSet();
        productMap.put(productA.getProductCode(), productA);
        productMap.put(productB.getProductCode(), productB);
        productMap.put(productC.getProductCode(), productC);
        productMap.put(productD.getProductCode(), productD);
    }

    @Test
    public void testTerminalSetPricing() {
        givenTerminalDoesNotContainAnyProducts();
        whenSettingPricingForProducts();
        thenProductsHaveCorrectData();
    }

    @Test
    public void testAddingProducts() {
        givenTerminalDoesNotContainAnyProducts();
        whenAddingProducts();
        thenTerminalContainsExpectedProducts();
    }

    @Test
    public void testTerminalScan() {
        givenTerminalContainsExpectedProducts();
        givenScannedProductsIsEmpty();
        whenScanningProductA();
        thenProductsScannedContainSingleA();
        thenSubtotalContainsValueOfSingleA();
        thenRunningCountEquals1();
        thenTotalEqualsValueOfSingleA();
    }

    @Test
    public void testNullProductException() {
        givenTerminalDoesNotContainAnyProducts();
        thenAddingNullProductThrowsNullProductException();
        thenScanThrowsNullProductException();
    }

    private void givenTerminalDoesNotContainAnyProducts() {
        terminal.setProducts(new HashMap<String, Product>());
    }

    private void whenSettingPricingForProducts() {
        terminal.setPricing("A", new BigDecimal("2.00"), new BigDecimal("7.00"), 4, LotDiscount.class);
        terminal.setPricing("B", new BigDecimal("12.00"), new BigDecimal("0.00"), 0, NoDiscount.class);
        terminal.setPricing("C", new BigDecimal("1.25"), new BigDecimal("6.00"), 6, LotDiscount.class);
        terminal.setPricing("D", new BigDecimal("0.15"), new BigDecimal("0.00"), 0, NoDiscount.class);
        terminal.setPricing("G", new BigDecimal("1.00"), new BigDecimal("0.90"), 10, ThresholdDiscount.class);
    }

    private void whenAddingProducts() {
        terminal.addProduct(productA);
        terminal.addProduct(productB);
        terminal.addProduct(productC);
        terminal.addProduct(productD);
    }

    private void whenScanningProductA() {
        terminal.scan("A");
    }

    private void thenProductsHaveCorrectData() {
        productA = terminal.getProductByProductCode("A");
        assertNotNull(productA);
        assertEquals(0, singlePriceA.compareTo(productA.getSinglePrice()));
        productB = terminal.getProductByProductCode("B");
        assertNotNull(productB);
        assertEquals(0, singlePriceB.compareTo(productB.getSinglePrice()));
        productC = terminal.getProductByProductCode("C");
        assertNotNull(productC);
        assertEquals(0, singlePriceC.compareTo(productC.getSinglePrice()));
        productD = terminal.getProductByProductCode("D");
        assertNotNull(productD);
        assertEquals(0, singlePriceD.compareTo(productD.getSinglePrice()));
    }

    private void thenTerminalContainsExpectedProducts() {
        assertTrue(terminal.getProducts().equals(productMap));
    }

    private void thenProductsScannedContainSingleA() {
        Map<String, Integer> productsScanned = terminal.getProductsScanned();
        assertEquals(1, productsScanned.size());
        for (Map.Entry<String, Integer> entry : productsScanned.entrySet()) {
            assertEquals("A", entry.getKey());
            assertEquals(new Integer(1), entry.getValue());
        }
    }

    private void thenSubtotalContainsValueOfSingleA() {
        BigDecimal runningTotal = terminal.getRunningTotal();
        BigDecimal singlePrice = productA.getSinglePrice();
        assertEquals(0, singlePrice.compareTo(runningTotal));
    }

    private void thenTotalEqualsValueOfSingleA() {
        assertEquals(0, productA.getSinglePrice().compareTo(terminal.total()));
    }

    private void thenRunningCountEquals1() {
        assertEquals(1, terminal.getRunningCountAllProductsScanned());
    }

    private void thenAddingNullProductThrowsNullProductException() {
        try {
            terminal.addProduct(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullProductException);
        }
    }

    private void thenScanThrowsNullProductException() {
        try {
            terminal.scan("A");
        } catch (Exception e) {
            assertTrue(e instanceof NullProductException);
        }
    }
}
