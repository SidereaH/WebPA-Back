package com.webpa.webpa;
import com.webpa.webpa.ProductCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.webpa.webpa.web.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductCardServiceTest {

    @Autowired
    private ProductCardService productCardService;

    private ProductCard testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new ProductCard();
        testProduct.setName("Test Product");
        testProduct.setPrice(100);
        testProduct.setDescription("Test Description");
    }

    @Test
    void saveProductTest() {
        ProductCard saved = productCardService.save(testProduct);
        assertNotNull(saved.getId());
        assertEquals("Test Product", saved.getName());
    }

    @Test
    void findByIdTest() {
        ProductCard saved = productCardService.save(testProduct);
        assertTrue(productCardService.findById(saved.getId()).isPresent());
    }

    @Test
    void findAllTest() {
        productCardService.save(testProduct);
        Page<ProductCard> products = productCardService.findAll(PageRequest.of(0, 10));
        assertFalse(products.isEmpty());
    }

    @Test
    void searchProductsTest() {
        productCardService.save(testProduct);
        var results = productCardService.findByNameContainingAndPriceBetween(
                "Test", 0, 1000);
        assertFalse(results.isEmpty());
        assertEquals("Test Product", results.get(0).getName());
    }
}