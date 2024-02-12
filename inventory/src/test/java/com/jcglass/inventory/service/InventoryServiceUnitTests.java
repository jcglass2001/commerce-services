package com.jcglass.inventory.service;

import com.jcglass.inventory.dto.ProductInventoryDTO;
import com.jcglass.inventory.model.Inventory;
import com.jcglass.inventory.repo.InventoryRepository;
import com.jcglass.inventory.util.ProductSkuGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class InventoryServiceUnitTests {
    @MockBean
    private InventoryRepository inventoryRepository;
    @Mock
    private ProductSkuGenerator productSkuGenerator;
    @Autowired
    private InventoryService inventoryService;

    @Test
    void registerProduct_ReturnsString(){
        //Arrange
        ProductInventoryDTO requestDTO = ProductInventoryDTO.builder()
                .productId("abc123")
                .categories(List.of("category1,category2"))
                .build();
        Inventory newInventory = Inventory.builder()
                .productId(requestDTO.getProductId())
                .skuCode(productSkuGenerator.generateSku(requestDTO.getCategories()))
                .build();
        //Mock repository calls
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(newInventory);

        //Act
        inventoryService.registerProduct(requestDTO);
        //Assert
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }


}