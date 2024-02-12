package com.jcglass.inventory.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@Component
public class ProductSkuGenerator implements SkuGenerator {
    private final Random random = new Random();
    private static final int SUFFIX_LENGTH = 4;
    @Override
    public String generateSku(List<String> productCategory) {
        String skuCode =  productCategory.stream()
                .map(category -> category.trim().substring(0,2).toUpperCase())
                .collect(Collectors.joining("-"));
        return skuCode + generateRandomSuffix();
    }

    private String generateRandomSuffix() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < ProductSkuGenerator.SUFFIX_LENGTH; i++){
            builder.append(random.nextInt(20));
        }
        return builder.toString();
    }
}
