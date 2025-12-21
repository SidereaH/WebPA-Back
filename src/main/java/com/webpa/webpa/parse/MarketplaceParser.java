package com.webpa.webpa.parse;

import com.webpa.webpa.ProductCard;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface MarketplaceParser {
    List<ProductCard> parseProducts(String url);
}