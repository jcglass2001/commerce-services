package com.jcglass.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {

    private String name;
    private List<String> categories;
    private List<String> images;
    private String url;
    private String description;
    private BigDecimal price;
}
