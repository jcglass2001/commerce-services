package com.jcglass.order.client;

import com.jcglass.order.dto.InventoryResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

//@FeignClient("inventory-service")
//@Component
@HttpExchange
public interface InventoryClient {
    @GetExchange("api/v1/inventory/check-stock")
    List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCodeList);

    @RequestMapping("/test")
    String testResponse();
}
