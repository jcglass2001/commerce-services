package com.jcglass.product.repo;

import com.jcglass.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByName(String name);
}
