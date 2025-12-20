package com.webpa.dto;

import com.webpa.domain.enums.Marketplace;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ParsedProduct(
        Marketplace marketplace,
        String sourceUrl,
        String name,
        BigDecimal price,
        String imageUrl,
        Double rating,
        Integer feedbacksCount,
        String seller,
        Double supplierRating,
        Integer available,
        String description,
        Map<String, Object> mainInfo,
        Map<String, String> rawAttributes,
        Map<String, String> normalizedAttributes,
        List<String> images,
        String excelFilename
) {
}
