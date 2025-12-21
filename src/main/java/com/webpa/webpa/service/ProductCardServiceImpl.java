package com.webpa.webpa.service;

import com.webpa.webpa.controllers.ProductCardService;
import com.webpa.webpa.repository.ProductCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import com.webpa.webpa.models.ProductCard;

@Service
@Transactional
@Slf4j
public class ProductCardServiceImpl implements ProductCardService {
    private final ProductCardRepository productCardRepository;
    private final ExcelExportService excelExportService;

    @Autowired
    public ProductCardServiceImpl(ProductCardRepository productCardRepository, 
                                ExcelExportService excelExportService) {
        this.productCardRepository = productCardRepository;
        this.excelExportService = excelExportService;
    }


    public Boolean existsByMarketplaceId(String id) {
        return productCardRepository.existsByMarketplaceId(id);
    }

    @Override
    public Long findIdByMarketplaceIdAndName(String marketplaceId, String name) {
        return productCardRepository.findIdByMarketplaceIdAndName(marketplaceId, name);
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
    public List<ProductCard> findByPriceRangeAndMarketplace(
            double minPrice, double maxPrice, String marketplace) {
        if (marketplace != null && !marketplace.isEmpty()) {
            return productCardRepository
                    .findByPriceBetweenAndMarketplaceIgnoreCase(minPrice, maxPrice, marketplace);
        }
        return productCardRepository.findByPriceBetween(minPrice, maxPrice);
    }
    @Override
    public ProductCard save(ProductCard product) {
        try {
            // First save the product to generate the ID
            ProductCard savedProduct = productCardRepository.save(product);
            
            // Then generate Excel file if not already present
            if (savedProduct.getExcelFile() == null) {
                byte[] excelFile = excelExportService.exportToExcel(List.of(savedProduct));
                savedProduct.setExcelFile(excelFile);
                savedProduct.setExcelFilename("product_" + System.currentTimeMillis() + ".xlsx");
                // Save again with the Excel file
                return productCardRepository.save(savedProduct);
            }
            
            return savedProduct;
        } catch (Exception e) {
            log.error("Error saving product: ", e);
            throw new RuntimeException("Failed to save product", e);
        }
    }

    @Override
    public List<ProductCard> saveAll(List<ProductCard> products) {
        try {
            // First save all products to generate IDs
            List<ProductCard> savedProducts = productCardRepository.saveAll(products);
            
            // Generate Excel file for all saved products
            byte[] excelFile = excelExportService.exportToExcel(savedProducts);
            
            // Set Excel file for each product
            savedProducts.forEach(product -> {
                product.setExcelFile(excelFile);
                product.setExcelFilename("products_" + System.currentTimeMillis() + ".xlsx");
            });
            
            // Save again with Excel files
            return productCardRepository.saveAll(savedProducts);
        } catch (Exception e) {
            log.error("Error saving products: ", e);
            throw new RuntimeException("Failed to save products", e);
        }
    }
}