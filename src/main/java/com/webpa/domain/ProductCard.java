package com.webpa.domain;

import com.webpa.domain.enums.Marketplace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_cards")
public class ProductCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private ParseTask parseTask;

    @Column(name = "query_text", length = 512)
    private String queryText;

    @Enumerated(EnumType.STRING)
    @Column(name = "marketplace", nullable = false, length = 64)
    private Marketplace marketplace;

    @Column(name = "source_url", length = 1024)
    private String sourceUrl;

    @Column(name = "name", nullable = false, length = 512)
    private String name;

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "feedbacks_count")
    private Integer feedbacksCount;

    @Column(name = "seller", length = 256)
    private String seller;

    @Column(name = "supplier_rating")
    private Double supplierRating;

    @Column(name = "available")
    private Integer available;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "main_info", columnDefinition = "jsonb")
    private Map<String, Object> mainInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "normalized_attributes", columnDefinition = "jsonb")
    private Map<String, String> normalizedAttributes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_attributes", columnDefinition = "jsonb")
    private Map<String, String> rawAttributes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "images", columnDefinition = "jsonb")
    private List<String> images;

    @Column(name = "excel_filename", length = 255)
    private String excelFilename;

    @Column(name = "collected_at", nullable = false)
    private Instant collectedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        collectedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
