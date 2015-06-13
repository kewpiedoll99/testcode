package com.barclayadunn.lab49;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: barclayadunn
 * Date: 6/13/15
 * Time: 2:46 PM
 */
public class ProductPricesStore {

    private List<String> productNames;
    private Map<String, List<BigDecimal>> productPriceListMap;

    private static ProductPricesStore instance = null;
    protected ProductPricesStore() {
    }

    public static ProductPricesStore getInstance(String[] productNames) {
        if (instance == null) {
            instance = new ProductPricesStore();
            instance.productNames = Arrays.asList(productNames);

            instance.productPriceListMap = new HashMap<String, List<BigDecimal>>();
            for (String productName : productNames) {
                instance.productPriceListMap.put(productName, new ArrayList<BigDecimal>());
            }
        }
        return instance;
    }

    public synchronized Map<String, List<BigDecimal>> getProductPriceListMap() {
        return productPriceListMap;
    }

    public synchronized void putProductPriceListInMap(String productName, List<BigDecimal> priceList) {
        productPriceListMap.put(productName, priceList);
    }

    public boolean hasProductName(String productName) {
        return productNames.contains(productName);
    }
}
