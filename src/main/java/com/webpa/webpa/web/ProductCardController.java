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
import java.util.List;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Optional;
import java.util.*;
// @Tag(name = "Product Card API", description = "Operations for product cards management")


@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductCardController {

    private final ProductCardService productCardService;

    @Autowired
    public ProductCardController(ProductCardService productCardService) {
        this.productCardService = productCardService;
    }

    // Create new product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductCard> createProduct(@Valid @RequestBody ProductCard product) {
        try {
            ProductCard createdProduct = productCardService.save(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating product: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all products with pagination
    @GetMapping
    public ResponseEntity<Page<ProductCard>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Page<ProductCard> products = productCardService.findAll(
                    PageRequest.of(page, size, Sort.by(sortBy)));
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving products: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductCard> getProductById(@PathVariable Long id) {
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
    public ResponseEntity<ProductCard> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductCard product) {
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
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
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
    public ResponseEntity<List<ProductCard>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "999999999") double maxPrice) {
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