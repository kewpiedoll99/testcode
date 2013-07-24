package com.barclayadunn;

import com.barclayadunn.exception.NullProductException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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
        terminal.setPricing("A", 2.00, 4, 7.00);
        terminal.setPricing("B", 12.00, 0, 0.00);
        terminal.setPricing("C", 1.25, 6, 6.00);
        terminal.setPricing("D", 0.15, 0, 0.00);
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
        assertEquals((Double) productA.getSinglePrice(), (Double) singlePriceA);
        productB = terminal.getProductByProductCode("B");
        assertNotNull(productB);
        assertEquals((Double) productB.getSinglePrice(), (Double) singlePriceB);
        productC = terminal.getProductByProductCode("C");
        assertNotNull(productC);
        assertEquals((Double) productC.getSinglePrice(), (Double) singlePriceC);
        productD = terminal.getProductByProductCode("D");
        assertNotNull(productD);
        assertEquals((Double) productD.getSinglePrice(), (Double) singlePriceD);
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
        assertEquals((Double) productA.getSinglePrice(), (Double) terminal.getRunningTotal());
    }

    private void thenTotalEqualsValueOfSingleA() {
        assertEquals((Double) productA.getSinglePrice(), (Double) terminal.total());
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
