package com.webpa.webpa.models;

import com.webpa.webpa.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

@Entity
@Table(name = "product_card")
@Data
public class ProductCard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marketplaceId;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(name = "img_url")
    private String image;

    @Column(name = "main_info", columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> mainInfo = new HashMap<>();

    @Column(name = "main_characteristics", columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> mainCharacteristics = new HashMap<>();

    @Column(name = "additional_information", columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> additionalInformation = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String marketplace;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Lob
    @Column(name = "excel_file", columnDefinition = "MEDIUMBLOB")
    private byte[] excelFile;

    @Column(name = "excel_filename")
    private String excelFilename;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Exclude excel file from JSON serialization
    @JsonIgnore
    public byte[] getExcelFile() {
        return excelFile;
    }
}