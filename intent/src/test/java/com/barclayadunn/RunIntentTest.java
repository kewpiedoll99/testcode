package com.barclayadunn;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        intentTestSets.put(1, "ABCDABAA");
        intentTestSets.put(2, "CCCCCCC");
        intentTestSets.put(3, "ABCD");
        intentTestAnswers.put(1, 32.40);
        intentTestAnswers.put(2, 7.25);
        intentTestAnswers.put(3, 15.40);
    }

    @Test
    public void testIntentTestCodeSets() {
        givenTerminalContainsExpectedProducts();
        for (int i = 1; i < 4; i++) {
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
        assertEquals(intentTestAnswers.get(i), (Double) terminal.total());
    }
}
