package com.jcglass.product.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcglass.product.dto.ProductRequestProperty;
import com.jcglass.product.model.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class ProductDataGenerator {
    @Getter
    private static  List<Product> productList;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    static {
        try (InputStream inputStream = new ClassPathResource("product_details.json").getInputStream()){
            JsonNode node = objectMapper.readTree(inputStream);
            productList = objectMapper.convertValue(node, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class,Product.class));
        } catch (IOException e){
            System.out.println("Failed to load product details.");
            e.printStackTrace();
        }
    }

    public String generateName(){
        return getNames().get(random.nextInt(getNames().size()));
    }
    public String generateUrl(){
        return getUrls().get(random.nextInt(getUrls().size()));
    }
    public String generateCategory(){
        return (String) getCategories().get(random.nextInt(getCategories().size()));
    }
    public String generateImage(){
        return (String) getImages().get(random.nextInt(getImages().size()));
    }
    public List<String> getNames(){
        return getProductProertyList(ProductRequestProperty.NAME, String.class);
    }
    public List<String> getUrls(){
        return getProductProertyList(ProductRequestProperty.URL, String.class);
    }
    public  List<String> getCategories(){
        return getProductProertyList(ProductRequestProperty.CATEGORY, List.class)
                .stream()
                .flatMap(List::stream)
                .distinct()
                .toList();
    }
    public List<String> getImages(){
        return getProductProertyList(ProductRequestProperty.IMAGE, List.class)
                .stream()
                .flatMap(List::stream)
                .distinct()
                .toList();
    }
    public <T> List<T> getProductProertyList(ProductRequestProperty property, Class<T> classType){
        return productList.stream()
                .map(product -> getProperty(product,property,classType))
                .toList();
    }
    public <T> T getProperty(Product product, ProductRequestProperty property, Class<T> classType){
        return switch (property){
            case NAME -> classType.cast(product.getName());
            case IMAGE -> classType.cast(product.getImages());
            case CATEGORY -> classType.cast(product.getCategories());
            case URL -> classType.cast(product.getUrl());
            case DESCRIPTION -> classType.cast(product.getDescription());
            case PRICE -> classType.cast(product.getPrice());
        };
    }
}
