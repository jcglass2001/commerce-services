package com.jcglass.inventory.service;

import com.jcglass.inventory.dto.InventoryResponse;
import com.jcglass.inventory.dto.ProductInventoryDTO;
import com.jcglass.inventory.model.Inventory;
import com.jcglass.inventory.repo.InventoryRepository;
import com.jcglass.inventory.util.ProductSkuGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final ProductSkuGenerator productSkuGenerator;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodeList){

        return inventoryRepository.findBySkuCodeIn(skuCodeList).stream()
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build()
                ).toList();
    }
    public void registerProduct(ProductInventoryDTO productInventoryDTO){
        Inventory newInventory = Inventory.builder()
                .productId(productInventoryDTO.getProductId())
                .skuCode(productSkuGenerator.generateSku(productInventoryDTO.getCategories()))
                .build();

        inventoryRepository.save(newInventory);
        log.info("Inventory Entity saved to repository: {}", newInventory);
    }
}
