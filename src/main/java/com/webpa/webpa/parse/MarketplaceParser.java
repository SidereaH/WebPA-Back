package com.webpa.webpa.parse;

import com.webpa.webpa.models.ProductCard;

import java.util.List;

public interface MarketplaceParser {
    List<ProductCard> parseProducts(String url);
}