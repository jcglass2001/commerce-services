package com.jcglass.order.service;

import com.jcglass.order.client.InventoryClient;
import com.jcglass.order.dto.InventoryResponse;
import com.jcglass.order.dto.OrderLineItemsDto;
import com.jcglass.order.dto.OrderRequest;
import com.jcglass.order.exception.custom.ItemNotInStockException;
import com.jcglass.order.model.Order;
import com.jcglass.order.model.OrderItem;
import com.jcglass.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient client;
    public void placeOrder(OrderRequest request){

        List<OrderItem> orderItemList = request.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto)
                .toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderItemList(orderItemList)
                .build();

        // retrieve skuCodes form orderItemList
        List<String> orderItemsSkuCodes = order.getOrderItemList().stream()
                .map(OrderItem::getSkuCode)
                .toList();

        /*
         * call Inventory module with list of skuCodes; returns same list with added 'isInStock'
         * boolean property for each skuCode
        */
        List<InventoryResponse> inventoryResponseArray = client.isInStock(orderItemsSkuCodes);

        assert inventoryResponseArray != null;
        boolean allProductsInStock = inventoryResponseArray.stream().allMatch(InventoryResponse::getIsInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        }else{
            log.error("Order failed to save: ItemNotInStockException thrown: InventoryArray: {}", inventoryResponseArray);
            throw new ItemNotInStockException("One of the products not in stock", inventoryResponseArray);
        }
        log.info("Order saved with id: {}", order.getOrderNumber());
    }
    private OrderItem mapToDto(OrderLineItemsDto orderLineItemsDto){

        return OrderItem.builder()
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
