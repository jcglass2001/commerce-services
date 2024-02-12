package com.jcglass.product.controller;

import com.google.gson.Gson;
import com.jcglass.product.dto.ProductRequest;
import com.jcglass.product.model.Product;
import com.jcglass.product.repo.ProductRepository;
import com.jcglass.product.util.ProductDataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductControllerPersistenceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @Autowired
    private ProductRepository productRepository;
    private final ProductDataGenerator generator = new ProductDataGenerator();

    private final int INITIAL_SIZE = 10;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    @Test
    void connectionEstablished(){
        assertThat(mongoDBContainer.isCreated()).isTrue();
        assertThat(mongoDBContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp(){
        List<Product> productList = generator.getProductList()
                .stream()
                .limit(INITIAL_SIZE)
                .toList();
        productRepository.saveAll(productList);
    }
    @Test
    void shouldCreateProduct() throws Exception {
        String serializedProductRequest = gson.toJson(getProductRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedProductRequest)
        ).andExpect(status().isCreated());

        Assertions.assertEquals(INITIAL_SIZE + 1, productRepository.findAll().size());
    }

    @Test
    void shouldGetProduct() throws Exception {
        String serializedProductRequest = gson.toJson(getProductRequest());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/getProducts"))
                .andExpect(status().isOk());
        assertThat(productRepository.count() == INITIAL_SIZE).isTrue();
    }

    private ProductRequest getProductRequest() {
        return generateProductRequest();
    }
    private ProductRequest generateProductRequest(){
        Random random = new Random();
        return ProductRequest.builder()
                .name(generator.generateName())
                .categories(List.of(generator.generateCategory()))
                .images(List.of(generator.generateImage()))
                .url(generator.generateUrl())
                .description("...")
                .price(BigDecimal.valueOf(random.nextInt()))
                .build();

    }
}