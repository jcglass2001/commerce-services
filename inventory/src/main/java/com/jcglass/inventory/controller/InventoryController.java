package com.jcglass.inventory.controller;

import com.jcglass.inventory.dto.InventoryResponse;
import com.jcglass.inventory.dto.ProductInventoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jcglass.inventory.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;
    @GetMapping("/check-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCodeList) {
        log.info("isInStock endpoint called with parameter {}", skuCodeList);
        return inventoryService.isInStock(skuCodeList);
    }

    @PostMapping("/registerProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerProduct(@RequestBody ProductInventoryDTO productInventoryDTO) {
        log.info("InventoryController | registerProduct endpoint reached with parameter {}", productInventoryDTO);
        inventoryService.registerProduct(productInventoryDTO);
        return new ResponseEntity<>("Product registered successfully",HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public String testResponse(){
        return "Response from inventory service";
    }
}
