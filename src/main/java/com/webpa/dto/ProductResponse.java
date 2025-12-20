package com.webpa.dto;

import com.webpa.domain.ProductCard;
import com.webpa.domain.enums.Marketplace;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID taskId,
        String queryText,
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
        Map<String, String> normalizedAttributes,
        Map<String, String> rawAttributes,
        List<String> images,
        String excelFilename,
        Instant collectedAt,
        Instant updatedAt
) {
    public static ProductResponse fromEntity(ProductCard card) {
        return new ProductResponse(
                card.getId(),
                card.getParseTask() != null ? card.getParseTask().getId() : null,
                card.getQueryText(),
                card.getMarketplace(),
                card.getSourceUrl(),
                card.getName(),
                card.getPrice(),
                card.getImageUrl(),
                card.getRating(),
                card.getFeedbacksCount(),
                card.getSeller(),
                card.getSupplierRating(),
                card.getAvailable(),
                card.getDescription(),
                card.getMainInfo(),
                card.getNormalizedAttributes(),
                card.getRawAttributes(),
                card.getImages(),
                card.getExcelFilename(),
                card.getCollectedAt(),
                card.getUpdatedAt()
        );
    }
}
