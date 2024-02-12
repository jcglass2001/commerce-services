package com.jcglass.product.controller;

import com.jcglass.product.dto.ProductRequest;
import com.jcglass.product.dto.ProductResponse;
import com.jcglass.product.service.ProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        log.info("ProductController | createProduct endpoint reached");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(productRequest));
    }
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") @NotNull String productId){
        log.info("ProductController | getProduct endpoint reached");
        return ResponseEntity.ok(productService.getProductById(productId));
    }
    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        log.info("ProductController | getAllProducts endpoint reached");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/updateByName/{productName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> updateProductByName(
            @PathVariable(value = "productName") @NotNull String productName,
            ProductRequest productRequest)
    {
        log.info("ProductController | updateProductByName endpoint reached");
        return ResponseEntity.ok(productService.updateProductByName(productName, productRequest));
    }

    @PutMapping("/updateById/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> updateProductById(
            @PathVariable(value = "productId") @NotNull String productId,
            ProductRequest productRequest)
    {
        log.info("ProductController | updateProductById endpoint reached");
        return ResponseEntity.ok(productService.updateProductById(productId, productRequest));
    }


    @DeleteMapping("/delete/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteProductById(@PathVariable(value = "productId") @NotNull String productId){
        log.info("ProductController | deleteProductById endpoint reached");
        return ResponseEntity.ok(productService.deleteProductById(productId));
    }
}
