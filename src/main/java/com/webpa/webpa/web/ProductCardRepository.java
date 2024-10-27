package com.webpa.webpa.web;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.webpa.webpa.ProductCard;

@Repository
public interface ProductCardRepository extends JpaRepository<ProductCard, Long> {
    List<ProductCard> findByNameContainingIgnoreCaseAndPriceBetween(
            String name, double minPrice, double maxPrice);
            
    List<ProductCard> findByPriceBetweenAndMarketplaceIgnoreCase(
            double minPrice, double maxPrice, String marketplace);
            
    List<ProductCard> findByPriceBetween(double minPrice, double maxPrice);
}