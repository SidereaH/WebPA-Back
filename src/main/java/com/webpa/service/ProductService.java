package com.webpa.service;

import com.webpa.domain.ProductCard;
import com.webpa.domain.enums.Marketplace;
import com.webpa.dto.ProductResponse;
import com.webpa.repository.ProductCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductCardRepository productCardRepository;

    public ProductService(ProductCardRepository productCardRepository) {
        this.productCardRepository = productCardRepository;
    }

    @Transactional
    public Page<ProductResponse> search(String query, List<Marketplace> marketplaces, Pageable pageable) {
        List<Marketplace> filter = marketplaces == null || marketplaces.isEmpty()
                ? Arrays.asList(Marketplace.values())
                : marketplaces;
        Page<ProductCard> page = productCardRepository.search(filter, query, pageable);
        return page.map(ProductResponse::fromEntity);
    }

    @Transactional
    public Page<ProductResponse> byTask(UUID taskId, Pageable pageable) {
        return productCardRepository.findByParseTaskId(taskId, pageable)
                .map(ProductResponse::fromEntity);
    }
}
