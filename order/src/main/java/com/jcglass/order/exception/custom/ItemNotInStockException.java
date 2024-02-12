package com.jcglass.order.exception.custom;


import java.util.List;

public class ItemNotInStockException extends RuntimeException{
    public ItemNotInStockException(String message){ super(message); }
    public ItemNotInStockException(String message, List<?> orderList){ super(message); }
}
