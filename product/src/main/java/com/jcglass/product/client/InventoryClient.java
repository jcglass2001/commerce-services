package com.jcglass.product.client;

import com.jcglass.product.dto.ProductInventoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.service.annotation.PostExchange;

public interface InventoryClient {
    @PostExchange("api/v1/inventory/registerProduct")
    ResponseEntity<String> registerProduct(@RequestBody ProductInventoryDTO productInventoryDTO);

    @RequestMapping("/test")
    String testResponse();
}
