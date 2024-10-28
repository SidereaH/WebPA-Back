package com.webpa.webpa;



import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductCardTest {

    private Validator validator;
    private ProductCard productCard;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        productCard = new ProductCard();
    }

    @Test
    void whenAllFieldsAreValid_thenNoValidationViolations() {
        // Given
        productCard.setName("Test Product");
        productCard.setPrice(100);
        productCard.setImage("http://example.com/image.jpg");
        
        HashMap<String, Object> mainInfo = new HashMap<>();
        mainInfo.put("brand", "Test Brand");
        productCard.setMainInfo(mainInfo);

        // When
        Set<ConstraintViolation<ProductCard>> violations = validator.validate(productCard);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        // Given
        productCard.setName("");
        productCard.setPrice(100);

        // When
        Set<ConstraintViolation<ProductCard>> violations = validator.validate(productCard);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Product name is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenNameIsTooShort_thenValidationFails() {
        // Given
        productCard.setName("A");
        productCard.setPrice(100);

        // When
        Set<ConstraintViolation<ProductCard>> violations = validator.validate(productCard);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("size must be between 2 and 50"));
    }

    @Test
    void whenNameIsTooLong_thenValidationFails() {
        // Given
        productCard.setName("A".repeat(51));
        productCard.setPrice(100);

        // When
        Set<ConstraintViolation<ProductCard>> violations = validator.validate(productCard);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("size must be between 2 and 50"));
    }

    @Test
    void whenPriceIsNegative_thenValidationFails() {
        // Given
        productCard.setName("Test Product");
        productCard.setPrice(-100);

        // When
        Set<ConstraintViolation<ProductCard>> violations = validator.validate(productCard);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("must be greater than 0"));
    }

    @Test
    void whenPriceIsZero_thenValidationFails() {
        // Given
        productCard.setName("Test Product");
        productCard.setPrice(0);

        // When
        Set<ConstraintViolation<ProductCard>> violations = validator.validate(productCard);

        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("must be greater than 0"));
    }

    @Test
    void testMainInfoMapOperations() {
        // Given
        HashMap<String, Object> mainInfo = new HashMap<>();
        mainInfo.put("key1", "value1");
        mainInfo.put("key2", 123);

        // When
        productCard.setMainInfo(mainInfo);

        // Then
        assertEquals(2, productCard.getMainInfo().size());
        assertEquals("value1", productCard.getMainInfo().get("key1"));
        assertEquals(123, productCard.getMainInfo().get("key2"));
    }

    @Test
    void testMainCharacteristicsMapOperations() {
        // Given
        HashMap<String, Object> characteristics = new HashMap<>();
        characteristics.put("weight", "1kg");
        characteristics.put("color", "red");

        // When
        productCard.setMainCharacteristics(characteristics);

        // Then
        assertEquals(2, productCard.getMainCharacteristics().size());
        assertEquals("1kg", productCard.getMainCharacteristics().get("weight"));
        assertEquals("red", productCard.getMainCharacteristics().get("color"));
    }

    @Test
    void testAdditionalInformationMapOperations() {
        // Given
        HashMap<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("warranty", "2 years");
        additionalInfo.put("manufacturer", "Company XYZ");

        // When
        productCard.setAdditionalInformation(additionalInfo);

        // Then
        assertEquals(2, productCard.getAdditionalInformation().size());
        assertEquals("2 years", productCard.getAdditionalInformation().get("warranty"));
        assertEquals("Company XYZ", productCard.getAdditionalInformation().get("manufacturer"));
    }

    @Test
    void testUrlAndMarketplaceFields() {
        // Given
        String testUrl = "https://example.com/product";
        String testMarketplace = "Test Marketplace";

        // When
        productCard.setUrl(testUrl);
        productCard.setMarketplace(testMarketplace);

        // Then
        assertEquals(testUrl, productCard.getUrl());
        assertEquals(testMarketplace, productCard.getMarketplace());
    }

    @Test
    void testDescriptionField() {
        // Given
        String testDescription = "This is a test product description";

        // When
        productCard.setDescription(testDescription);

        // Then
        assertEquals(testDescription, productCard.getDescription());
    }
}