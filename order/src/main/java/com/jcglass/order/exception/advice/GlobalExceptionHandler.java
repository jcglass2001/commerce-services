package com.jcglass.order.exception.advice;

import com.jcglass.order.exception.custom.ItemNotInStockException;
import com.jcglass.order.exception.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotInStockException.class)
    public ResponseEntity<ErrorResponse> handleItemNotInStockException(ItemNotInStockException exception){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errMessage(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
