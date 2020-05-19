package com.barclayadunn.intent;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 7:42 PM
 */
public class RunIntentTest extends TestData {

    @Before
    public void setup() {
        terminal = new Terminal();
        givenProductSet();
        productMap.put(productA.getProductCode(), productA);
        productMap.put(productB.getProductCode(), productB);
        productMap.put(productC.getProductCode(), productC);
        productMap.put(productD.getProductCode(), productD);
        productMap.put(productG.getProductCode(), productG);
        intentTestSets.put(1, "ABCDABAA");
        intentTestSets.put(2, "CCCCCCC");
        intentTestSets.put(3, "ABCD");
        intentTestSets.put(4, "GGGGGGGGGGGG");
        intentTestAnswers.put(1, new BigDecimal("32.40"));
        intentTestAnswers.put(2, new BigDecimal("7.25"));
        intentTestAnswers.put(3, new BigDecimal("15.40"));
        intentTestAnswers.put(4, new BigDecimal("10.80"));
    }

    @Test
    public void testIntentTestCodeSets() {
        givenTerminalContainsExpectedProducts();
        for (Integer i : intentTestSets.keySet()) {
            givenScannedProductsIsEmpty();
            whenScanningIntentTestSet(i);
            thenTerminalTotalEqualsExpectedTotal(i);
        }
    }

    private void whenScanningIntentTestSet(int i) {
        String intentTestString = intentTestSets.get(i);
        for (Character c : intentTestString.toCharArray()) {
            terminal.scan(c.toString());
        }
    }

    private void thenTerminalTotalEqualsExpectedTotal(int i) {
        assertEquals(0, intentTestAnswers.get(i).compareTo(terminal.total()));
    }
}
