package com.jcglass.product.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
public class ProductRequest {
    private String name;
    private List<String> images;
    private List<String> categories;
    private String url;
    private String description;
    private BigDecimal price;
}