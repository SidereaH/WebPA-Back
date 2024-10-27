package com.webpa.webpa.web;

import com.webpa.webpa.ProductCard;
import com.webpa.webpa.service.ExcelExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Slf4j
@Tag(name = "Product Card API", description = "Operations for managing product cards")
public class ProductCardController {

    private final ProductCardService productCardService;
    private final ExcelExportService excelExportService;

    @Autowired
    public ProductCardController(ProductCardService productCardService, ExcelExportService excelExportService) {
        this.productCardService = productCardService;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/fetchAll")
    @Operation(summary = "Fetch all products", description = "Returns all products from database")
    public ResponseEntity<List<ProductCard>> getAllProducts() {
        try {
            List<ProductCard> products = productCardService.findAll(PageRequest.of(0, Integer.MAX_VALUE))
                    .getContent();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error fetching all products: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/export/excel")
    @Operation(summary = "Export to Excel", description = "Export all products to Excel file")
    public ResponseEntity<byte[]> exportToExcel() {
        try {
            List<ProductCard> products = productCardService.findAll(PageRequest.of(0, Integer.MAX_VALUE))
                    .getContent();
            byte[] excelFile = excelExportService.exportToExcel(products);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "products.xlsx");

            return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error exporting to Excel: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sort")
    @Operation(summary = "Get sorted products", description = "Returns products sorted by specified field")
    public ResponseEntity<Page<ProductCard>> getSortedProducts(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Products per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") String direction) {
        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
            Page<ProductCard> products = productCardService.findAll(
                    PageRequest.of(page, size, Sort.by(sortDirection, sortBy)));
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error fetching sorted products: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products", description = "Filter products by price range and marketplace")
    public ResponseEntity<List<ProductCard>> filterProducts(
            @Parameter(description = "Minimum price") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Marketplace") @RequestParam(required = false) String marketplace) {
        try {
            minPrice = minPrice != null ? minPrice : 0.0;
            maxPrice = maxPrice != null ? maxPrice : Double.MAX_VALUE;
            
            List<ProductCard> products = productCardService.findByPriceRangeAndMarketplace(
                    minPrice, maxPrice, marketplace);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error filtering products: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Остальные существующие методы остаются без изменений
}