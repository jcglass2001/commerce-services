package com.jcglass.product.service;

import com.jcglass.product.client.InventoryClient;
import com.jcglass.product.dto.ProductInventoryDTO;
import com.jcglass.product.dto.ProductRequest;
import com.jcglass.product.dto.ProductResponse;
import com.jcglass.product.exception.custom.ProductNotFoundException;
import com.jcglass.product.model.Product;
import com.jcglass.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    private final InventoryClient inventoryClient;
    private final ModelMapper modelMapper = new ModelMapper();


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapToProduct(productRequest);
        Product savedProduct = productRepository.save(product);
        log.info("ProductService | createProduct | Product saved: {} ", savedProduct);

        ProductInventoryDTO productInventoryDTO = ProductInventoryDTO.builder()
                .productId(savedProduct.getId())
                .categories(savedProduct.getCategories())
                .build();

        log.info("ProductService | createProduct | Registering product to Inventory Service: {}", productInventoryDTO);
        inventoryClient.registerProduct(productInventoryDTO);
        log.info("ProductService | createProduct | Product successfully registered");

        //todo: save newProduct.images to Amazon S3
        return mapToProductResponse(savedProduct);
    }
    public ProductResponse updateProductByName(String productName, ProductRequest productRequest){
        Product existingProduct = productRepository.findByName(productName);
        log.info("ProductService | updateProductByName | Product retrieved from repository");

        if(existingProduct == null){
            log.error("ProductService | updateProductByName | Error fetching product with name: {}", productName);
            throw new ProductNotFoundException("Product not found");
        }

        modelMapper.map(productRequest,existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("ProductService | updateProductByName| Updated product saved: {}", updatedProduct);

        return mapToProductResponse(updatedProduct);
    }
    public ProductResponse updateProductById(String productId, ProductRequest productRequest){
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        log.info("ProductService | updateProductById | Product retrieved from repository");

        modelMapper.map(productRequest,existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("ProductService | updateProductById | Updated product saved: {}", updatedProduct);

        return mapToProductResponse(updatedProduct);
    }
    public String deleteProductById(String productId){
        productRepository.deleteById(productId);
        log.info("ProductService | deleteProductById | Product with id: {} deleted",productId);
        return "Product Deleted";
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        log.info("ProductService | getAllProducts | Products list of length {} retrieved from repository", allProducts.size());

        return allProducts.stream()
                .map(this::mapToProductResponse)
                .toList();
    }
    public ProductResponse getProductById(String id){
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product not found"));
        log.info("ProductService | getProductById | Product retrieved from repository");

        return mapToProductResponse(product);
    }

    private ProductResponse mapToProductResponse(Product product){
        return modelMapper.map(product, ProductResponse.class);
    }
    private Product mapToProduct(ProductRequest productRequest){
        return Product.builder()
                .name(productRequest.getName())
                .images(productRequest.getImages())
                .categories(productRequest.getCategories())
                .url(productRequest.getUrl())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
    }
}
