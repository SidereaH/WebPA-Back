package com.webpa.webpa.parse.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webpa.webpa.ProductCard;
import com.webpa.webpa.parse.WildberriesParser;
import com.webpa.webpa.web.ProductCardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/parser")
@Slf4j
@Tag(name = "Parser API", description = "Parse product cards in markets")
public class ParserController {

    private final WildberriesParser wildberriesParser;
    private final ProductCardService productCardService;

    @Autowired
    public ParserController(WildberriesParser wildberriesParser, ProductCardService productCardService) {
        this.wildberriesParser = wildberriesParser;
        this.productCardService = productCardService;
    }
    @Operation(summary = "Получить 100 товаров из маркетплейсов по запросу", description = "Парсит и отправляет данные о товарах")
    @PostMapping("/search")
    public ResponseEntity<?> searchAndSaveProducts(@RequestParam String query) {
        try {
            log.info("Starting search for query: {}", query);
            
            // Parse products from Wildberries
            List<ProductCard> products = wildberriesParser.parseProducts(query);
            
            // Save all products to database
            products.forEach(product -> {
                try {
                    productCardService.save(product);
                } catch (Exception e) {
                    log.error("Error saving product: {}", e.getMessage());
                }
            });
            
            log.info("Successfully parsed and saved {} products", products.size());
            
            return ResponseEntity.ok()
                .body(new ParserResponse(
                    true,
                    String.format("Successfully parsed %d products", products.size()),
                    products
                ));
        } catch (Exception e) {
            log.error("Error during parsing: ", e);
            return ResponseEntity.internalServerError()
                .body(new ParserResponse(
                    false,
                    "Error during parsing: " + e.getMessage(),
                    null
                ));
        }
    }
}