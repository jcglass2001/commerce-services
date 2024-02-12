package com.jcglass.order;

import com.google.gson.Gson;
import com.jcglass.order.dto.OrderLineItemsDto;
import com.jcglass.order.dto.OrderRequest;
import com.jcglass.order.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
public class OrderApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @Autowired
    private OrderRepository orderRepository;
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:9.6.12")
            .withStartupTimeoutSeconds(30)
            .withDatabaseName("commerce_order")
            .withUsername("postgres")
            .withPassword("Gr33nT34isgross");
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    @BeforeEach
    void clearDatabase(){
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() throws Exception {
        String serializedOrderLineRequest = gson.toJson(getOrderRequest());
        System.out.println(serializedOrderLineRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedOrderLineRequest)
        ).andExpect(status().isCreated());
    }
    @Test
    void shouldNotCreateOrder() throws Exception {
        String serializedOrderLineRequest = gson.toJson(getOrderRequestWithEmptyQuantity());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedOrderLineRequest)
        ).andExpect(status().isInternalServerError());

    }

    private OrderRequest getOrderRequest(){
        List<OrderLineItemsDto> orderLineItems = new ArrayList<>();
        orderLineItems.add(generateOrderLineItemsDto("random_sku_1",BigDecimal.valueOf(1200),1));
        orderLineItems.add(generateOrderLineItemsDto("random_sku_2",BigDecimal.valueOf(13.99),2));

        return OrderRequest.builder()
                .orderLineItemsDtoList(orderLineItems)
                .build();
    }
    private OrderRequest getOrderRequestWithEmptyQuantity(){
        List<OrderLineItemsDto> orderLineItems = new ArrayList<>();
        orderLineItems.add(generateOrderLineItemsDto("random_sku_1",BigDecimal.valueOf(1200),1));
        orderLineItems.add(generateOrderLineItemsDto("random_sku_2",BigDecimal.valueOf(13.99),2));
        orderLineItems.add(generateOrderLineItemsDto("random_sku_3",BigDecimal.valueOf(15.99),0));

        return OrderRequest.builder()
                .orderLineItemsDtoList(orderLineItems)
                .build();
    }
    private OrderLineItemsDto generateOrderLineItemsDto(String sku, BigDecimal price, Integer quantity){
        return OrderLineItemsDto.builder()
                .skuCode(sku)
                .price(price)
                .quantity(quantity)
                .build();
    }


}
