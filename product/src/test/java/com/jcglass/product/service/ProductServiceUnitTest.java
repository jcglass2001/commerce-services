package com.jcglass.product.service;

import com.jcglass.product.dto.ProductRequest;
import com.jcglass.product.dto.ProductResponse;
import com.jcglass.product.exception.custom.ProductNotFoundException;
import com.jcglass.product.model.Product;
import com.jcglass.product.repo.ProductRepository;
import com.jcglass.product.util.ProductDataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    private final ProductDataGenerator generator = new ProductDataGenerator() ;

    @Test
    void createProduct_ReturnsProductResponse() {
        // Arrange
        Product product = generateProduct();
        ProductRequest request = mapToProductRequest(product);
        // Mock repository calls
        when(productRepository.insert(any(Product.class))).thenReturn(product);
        // Act
        ProductResponse result = productService.createProduct(request);
        // Assert
        assertNotNull(result);
        assertEquals(request.getName(),result.getName());
        assertEquals(request.getCategories(), result.getCategories());
        assertEquals(request.getImages(),result.getImages());
        assertEquals(request.getUrl(), result.getUrl());
        assertEquals(request.getDescription(),result.getDescription());
        assertEquals(request.getPrice(),result.getPrice());


        verify(productRepository, times(1)).insert(any(Product.class));
    }
    @Test
    void updateProductByName_UpdateCategory_ReturnsProductResponse(){
        // Arrange
        Product product = generateProduct();
        String productName = product.getName();

        List<String> newCategoryList = new ArrayList<>(product.getCategories());
        newCategoryList.set(3,"new_category");
        ProductRequest updateRequest = mapToProductRequest(product);
        updateRequest.setCategories(newCategoryList);


        // Mock repository calls
        when(productRepository.findByName(productName)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponse result = productService.updateProductByName(productName,updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newCategoryList,result.getCategories());

        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void updateProductByName_UpdateImages_ReturnsProductResponse(){
        // Arrange
        Product product = generateProduct();
        String productName = product.getName();

        List<String> newImageList = new ArrayList<>(product.getImages());
        newImageList.set(3,"updated_image");
        ProductRequest updateRequest = mapToProductRequest(product);
        updateRequest.setImages(newImageList);

        // Mock repository calls
        when(productRepository.findByName(productName)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponse result = productService.updateProductByName(productName,updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newImageList,result.getImages());

        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void updateProductByName_UpdateUrl_ReturnsProductResponse(){
        // Arrange
        Product product = generateProduct();
        String productName = product.getName();

        String newUrl = "http://updated-url";
        ProductRequest updateRequest = mapToProductRequest(product);
        updateRequest.setUrl(newUrl);

        // Mock repository calls
        when(productRepository.findByName(productName)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponse result = productService.updateProductByName(productName,updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newUrl,result.getUrl());

        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void updateProductByName_UpdateDescription_ReturnsProductResponse(){
        // Arrange
        Product product = generateProduct();
        String productName = product.getName();

        String newDescription = "something cool";
        ProductRequest updateRequest = mapToProductRequest(product);
        updateRequest.setDescription(newDescription);

        // Mock repository calls
        when(productRepository.findByName(productName)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponse result = productService.updateProductByName(productName,updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newDescription,result.getDescription());

        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void updateProductByName_UpdatePrice_ReturnsProductResponse(){
        // Arrange
        Product product = generateProduct();
        String productName = product.getName();

        BigDecimal newPrice = BigDecimal.valueOf(300);
        ProductRequest updateRequest = mapToProductRequest(product);
        updateRequest.setPrice(newPrice);

        // Mock repository calls
        when(productRepository.findByName(productName)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponse result = productService.updateProductByName(productName,updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newPrice,result.getPrice());

        verify(productRepository, times(1)).findByName(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    void updateProductByName_ReturnsException(){
        // Arrange
        String nonExistingProductName = "NonExistingProductName";
        ProductRequest updateRequest = generateProductRequest();
        // Mock repository calls
        when(productRepository.findByName(nonExistingProductName)).thenReturn(null);
        // Act and Assert
        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProductByName(nonExistingProductName, updateRequest));
    }

    private Product generateProduct(){
        Random random = new Random();
        return Product.builder()
                .name(generator.generateName())
                .categories(generator.getCategories().stream().limit(5).toList())
                .images(generator.getImages().stream().limit(5).toList())
                .url(generator.generateUrl())
                .description("...")
                .price(BigDecimal.valueOf(random.nextInt(0,200)))
                .build();
    }
    private ProductRequest generateProductRequest(){
        Random random = new Random();
        return ProductRequest.builder()
                .name(generator.generateName())
                .categories(List.of(generator.generateCategory()))
                .images(List.of(generator.generateImage()))
                .url(generator.generateUrl())
                .description("...")
                .price(BigDecimal.valueOf(random.nextInt()))
                .build();
    }

    private ProductRequest mapToProductRequest(Product product){
        return ProductRequest.builder()
                .name(product.getName())
                .categories(product.getCategories())
                .images(product.getImages())
                .url(product.getUrl())
                .description("...")
                .price(product.getPrice())
                .build();
    }
}