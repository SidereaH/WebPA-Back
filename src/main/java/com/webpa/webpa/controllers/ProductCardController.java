package com.webpa.webpa.controllers;

import com.webpa.webpa.models.ProductCard;
import com.webpa.webpa.service.ExcelExportService;
import com.webpa.webpa.service.ProductCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping
    @Operation(summary = "Save product with Excel", description = "Saves product and generates Excel file")
    public ResponseEntity<ProductCard> saveProduct(@RequestBody @Valid ProductCard product) {
        try {
            // Generate Excel file for single product
            byte[] excelFile = excelExportService.exportToExcel(List.of(product));
            
            // Set Excel file data to product
            product.setExcelFile(excelFile);
            product.setExcelFilename("product_" + System.currentTimeMillis() + ".xlsx");
            
            // Save product with Excel file
            ProductCard savedProduct = productCardService.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            log.error("Error saving product: ", e);
            return ResponseEntity.internalServerError().build();
        }
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

    @GetMapping("/excel/{id}")
    @Operation(summary = "Get Excel file", description = "Download Excel file for specific product")
    public ResponseEntity<byte[]> getExcelFile(@PathVariable Long id) {
        try {
            Optional<ProductCard> productOpt = productCardService.findById(id);
            if (productOpt.isPresent()) {
                ProductCard product = productOpt.get();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", product.getExcelFilename());
                return new ResponseEntity<>(product.getExcelFile(), headers, HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching Excel file: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}