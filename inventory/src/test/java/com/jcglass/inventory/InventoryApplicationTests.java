package com.jcglass.inventory;

import com.jcglass.inventory.repo.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InventoryRepository inventoryRepository;


    @Test
    void shouldReturnInStockResponse() throws Exception {
        String inStockItemSkuCode = "iphone_13";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory/checkInventory_?skuCode=" + inStockItemSkuCode)
        ).andExpect(status().isOk());
    }
}
