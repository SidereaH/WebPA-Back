package com.webpa.webpa;


import com.webpa.webpa.ProductCard;
import com.webpa.webpa.service.ExcelExportService;
import com.webpa.webpa.web.ProductCardService;

import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.webpa.webpa.web.*;
import java.util.Collections;
import java.util.Optional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductCardControllerTest {

    @InjectMocks
    private ProductCardController productCardController;

    @Mock
    private ProductCardService productCardService;

    @Mock
    private ExcelExportService excelExportService;

    private ProductCard productCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productCard = new ProductCard();
        productCard.setId(1L);
        productCard.setName("Test Product");
        productCard.setPrice(100.0);
    }

    @Test
    void getExcelFile_ShouldReturnNotFound_WhenProductDoesNotExist() {
        // Arrange
        when(productCardService.findById(1L)).thenReturn(Optional.empty());
        // Act
        ResponseEntity<byte[]> response = productCardController.getExcelFile(1L);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
