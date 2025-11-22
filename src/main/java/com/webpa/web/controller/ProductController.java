package com.webpa.web.controller;

import com.webpa.domain.enums.Marketplace;
import com.webpa.dto.ProductResponse;
import com.webpa.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductResponse> search(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "marketplace", required = false) List<Marketplace> marketplaces,
            @PageableDefault(size = 20, sort = "collectedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return productService.search(query, marketplaces, pageable);
    }

    @GetMapping("/task/{taskId}")
    public Page<ProductResponse> byTask(
            @PathVariable UUID taskId,
            @PageableDefault(size = 20, sort = "collectedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return productService.byTask(taskId, pageable);
    }
}
