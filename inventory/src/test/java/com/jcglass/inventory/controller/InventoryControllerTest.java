package com.jcglass.inventory.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcglass.inventory.dto.ProductInventoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final List<String> categories = loadCategories();
    private final Random random = new Random();
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    @DynamicPropertySource
    static void dynamicConfig(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }


    InventoryControllerTest() throws IOException {
    }

    @Test
    void testRegisterProduct() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ProductInventoryDTO productInventoryDTO = buildProductInventoryDTO();
        String jsonContent = objectMapper.writeValueAsString(productInventoryDTO);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/inventory/registerProduct")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private ProductInventoryDTO buildProductInventoryDTO(){;
        return ProductInventoryDTO.builder()
                .productId(UUID.randomUUID().toString())
                .categories(generateCategories(3))
                .build();
    }
    private List<String> loadCategories() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new ClassPathResource("product_categories.json").getFile();
        Map<String, List<String>> categoriesMap = objectMapper.readValue(file, new TypeReference<Map<String, List<String>>>() {});
        return categoriesMap.get("Product Categories");


    }
    private List<String> generateCategories(int size){
        return categories.stream().limit(size).toList();
    }

}