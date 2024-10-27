package com.webpa.webpa.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import com.webpa.webpa.ProductCard;

@Service
@Transactional
@Slf4j
public class ProductCardServiceImpl implements ProductCardService {

    private final ProductCardRepository productCardRepository;

    @Autowired
    public ProductCardServiceImpl(ProductCardRepository productCardRepository) {
        this.productCardRepository = productCardRepository;
    }

    @Override
    public ProductCard save(ProductCard product) {
        return productCardRepository.save(product);
    }

    @Override
    public Optional<ProductCard> findById(Long id) {
        return productCardRepository.findById(id);
    }

    @Override
    public Page<ProductCard> findAll(Pageable pageable) {
        return productCardRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        productCardRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productCardRepository.existsById(id);
    }

    @Override
    public List<ProductCard> findByNameContainingAndPriceBetween(
            String name, double minPrice, double maxPrice) {
        return productCardRepository
                .findByNameContainingIgnoreCaseAndPriceBetween(name, minPrice, maxPrice);
    }
    @Override
    public List<ProductCard> findByPriceRangeAndMarketplace(double minPrice, double maxPrice, String marketplace) {
        if (marketplace != null && !marketplace.isEmpty()) {
            return productCardRepository.findByPriceBetweenAndMarketplaceIgnoreCase(minPrice, maxPrice, marketplace);
        }
        return productCardRepository.findByPriceBetween(minPrice, maxPrice);
    }
}