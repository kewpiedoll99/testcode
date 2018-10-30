package com.barclayadunn;

import com.barclayadunn.exception.NullProductException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * User: barclayadunn
 * Date: 6/22/13
 * Time: 4:54 PM
 */
public class Terminal {

    private Map<String, Product> products = new HashMap<String, Product>();
    private Map<String, Integer> productsScanned = new HashMap<String, Integer>();

    public void setPricing(String productCode, BigDecimal singlePrice, BigDecimal lotPrice, int lotCount, Class discountClass) {
        Product p = getProductByProductCode(productCode);
        if (p == null) {
            if (LotDiscount.class.equals(discountClass))
                p = new Product(productCode, new LotDiscount(singlePrice, lotPrice, lotCount));
            if (NoDiscount.class.equals(discountClass))
                p = new Product(productCode, new NoDiscount(singlePrice, lotPrice, lotCount));
            if (ThresholdDiscount.class.equals(discountClass))
                p = new Product(productCode, new ThresholdDiscount(singlePrice, lotPrice, lotCount));
        }
        products.put(productCode, p);
    }

    public void scan(String productCode) throws NullProductException {
        if (!products.containsKey(productCode)) {
            throw new NullProductException("This product, " + productCode + ", is not in store inventory.");
        }
        int scanCount = 0;
        if (productsScanned.containsKey(productCode)) {
            scanCount = productsScanned.get(productCode);
        }
        scanCount++;
        productsScanned.put(productCode, scanCount);
        System.out.println("Scanned: " + productCode + "; running count: " + scanCount);
    }

    public BigDecimal getRunningTotal() {
        BigDecimal runningTotal = new BigDecimal("0.00");
        Product p;
        for (Map.Entry<String, Integer> entry : productsScanned.entrySet()) {
            p = getProductByProductCode(entry.getKey());
            BigDecimal priceForCount = p.getPriceForCount(entry.getValue());
            runningTotal = runningTotal.add(priceForCount);
        }
        return runningTotal;
    }

    public int getRunningCountAllProductsScanned() {
        int runningCount = 0;
        for (Map.Entry<String, Integer> entry : productsScanned.entrySet()) {
            runningCount += entry.getValue();
        }
        return runningCount;
    }

    public BigDecimal total() {
        BigDecimal totalAmount = getRunningTotal();
        int countProductsScanned = getRunningCountAllProductsScanned();
        System.out.printf("Total products purchased: %d\nTotal amount due: $%,.2f\n", countProductsScanned, totalAmount);
        return totalAmount;
    }

    public void setProducts(Map<String, Product> products) { this.products = products; }
    public Map<String, Product> getProducts() { return products; }
    public void addProduct(Product product) throws NullProductException {
        if (product == null) {
            throw new NullProductException("No product submitted");
        }
        products.put(product.getProductCode(), product);
    }

    public void setProductsScanned(Map<String, Integer> productsScanned) { this.productsScanned = productsScanned; }
    public Map<String, Integer> getProductsScanned() { return productsScanned; }

    public Product getProductByProductCode(String productCode) {
        if (products == null || products.size() == 0 || !products.keySet().contains(productCode)) {
            return null;
        }
        return products.get(productCode);
    }
}
