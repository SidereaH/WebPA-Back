package com.webpa.webpa.service;

import com.webpa.webpa.models.ProductCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductCardService {
    ProductCard save(ProductCard product);
    Optional<ProductCard> findById(Long id);
    Page<ProductCard> findAll(Pageable pageable);
    void deleteById(Long id);
    boolean existsById(Long id);
    Long findIdByMarketplaceIdAndName(String marketplaceId, String name);
    List<ProductCard> saveAll(List<ProductCard> product);
    Boolean existsByMarketplaceId(String marketplaceId);
}