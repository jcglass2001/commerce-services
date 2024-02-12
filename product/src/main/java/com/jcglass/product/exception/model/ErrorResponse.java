package com.jcglass.product.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private int errorCode;
    private String errorMessage;
    private LocalDateTime timestamp;
}
