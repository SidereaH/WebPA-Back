package com.webpa.webpa.parse.web;

import com.webpa.webpa.ProductCard;
import lombok.Data;
import java.util.List;

@Data
public class ParserResponse {
    private boolean success;
    private String message;
    private List<ProductCard> products;

    public ParserResponse(boolean success, String message, List<ProductCard> products) {
        this.success = success;
        this.message = message;
        this.products = products;
    }

}