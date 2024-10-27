package com.webpa.webpa.web;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.webpa.webpa.ProductCard;

@Repository
public interface ProductCardRepository extends JpaRepository<ProductCard, Long> {
    @Query("SELECT p FROM ProductCard p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "p.price BETWEEN :minPrice AND :maxPrice")
    List<ProductCard> findByNameContainingIgnoreCaseAndPriceBetween(
            @Param("name") String name, 
            @Param("minPrice") double minPrice, 
            @Param("maxPrice") double maxPrice);
            
    @Query("SELECT p FROM ProductCard p WHERE " +
           "p.price BETWEEN :minPrice AND :maxPrice AND " +
           "(:marketplace IS NULL OR LOWER(p.marketplace) = LOWER(:marketplace))")
    List<ProductCard> findByPriceBetweenAndMarketplaceIgnoreCase(
            @Param("minPrice") double minPrice, 
            @Param("maxPrice") double maxPrice, 
            @Param("marketplace") String marketplace);
            
    List<ProductCard> findByPriceBetween(double minPrice, double maxPrice);
}