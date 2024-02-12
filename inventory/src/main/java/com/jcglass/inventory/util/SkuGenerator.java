package com.jcglass.inventory.util;

import java.util.List;

public interface SkuGenerator {
    /**
     * Generates a unique SKU for product
     *
     * @param productCategory   List of categories of the product
     * @return  A generate SKU for the product
     */
    String generateSku(List<String> productCategory);
}
