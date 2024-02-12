package com.jcglass.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Product {

    @Id
    @JsonIgnore
    private String id;

    @JsonProperty("Product Name")
    @NotNull
    private String name;
    @JsonProperty("Image")
    private List<String> images;
    @JsonProperty("Category")
    private List<String> categories;
    @JsonProperty("Product Url")
    private String url;
    private String description;
    private BigDecimal price;

}
