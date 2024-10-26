package com.webpa.webpa.web;

import com.webpa.webpa.ProductCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Slf4j
@Tag(name = "Product Card API", description = "Operations for managing product cards")
public class ProductCardController {

    private final ProductCardService productCardService;

    @Autowired
    public ProductCardController(ProductCardService productCardService) {
        this.productCardService = productCardService;
    }
    // Create new product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog.")
    public ResponseEntity<ProductCard> createProduct(
            @Parameter(description = "Product details") @Valid @RequestBody ProductCard product) {
        try {
            ProductCard createdProduct = productCardService.save(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating product: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all products with pagination
    @Operation(summary = "Get all products with pagination", description = "Returns paginated list of products.")
    @GetMapping
    public ResponseEntity<Page<ProductCard>> getAllProducts(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of products per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "id") String sortBy) {
        {
            try {
                Page<ProductCard> products = productCardService.findAll(
                        PageRequest.of(page, size, Sort.by(sortBy)));
                return new ResponseEntity<>(products, HttpStatus.OK);
            } catch (Exception e) {
                log.error("Error retrieving products: ", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    // Get product by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a product by its ID.")
    public ResponseEntity<ProductCard> getProductById(
            @Parameter(description = "ID of the product to retrieve") @PathVariable Long id) {
        try {
            return productCardService.findById(id)
                    .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error("Error retrieving product with id {}: ", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update product
    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product by ID.")
    public ResponseEntity<ProductCard> updateProduct(
            @Parameter(description = "ID of the product to update") @PathVariable Long id,
            @Parameter(description = "Updated product details") @Valid @RequestBody ProductCard product) {
        try {
            if (!productCardService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            product.setId(id);
            ProductCard updatedProduct = productCardService.save(product);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating product with id {}: ", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete product
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by ID.")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete") @PathVariable Long id) {
        try {
            if (!productCardService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            productCardService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting product with id {}: ", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search products by name
    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search for products by name and price range.")
    public ResponseEntity<List<ProductCard>> searchProducts(

            @Parameter(description = "Name of the product to search") @RequestParam String name,
            @Parameter(description = "Minimum price") @RequestParam(defaultValue = "0") double minPrice,
            @Parameter(description = "Maximum price") @RequestParam(defaultValue = "999999999") double maxPrice) {
        try {
            List<ProductCard> products = productCardService
                    .findByNameContainingAndPriceBetween(name, minPrice, maxPrice);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error searching products: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Error handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}