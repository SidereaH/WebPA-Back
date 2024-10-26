package com.webpa.webpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "product_card")
@Data
public class ProductCard implements Serializable {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer price;

    @Column(name = "img_url")
    private String image;

    @Column(name = "main_info", columnDefinition = "TEXT")
    private String mainInfoJson;

    @Column(name = "main_characteristics", columnDefinition = "TEXT")
    private String mainCharacteristicsJson;

    @Column(name = "additional_information", columnDefinition = "TEXT")
    private String additionalInformationJson;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Transient
    private Map<String, Object> mainInfo = new HashMap<>();

    @Transient
    private Map<String, Object> mainCharacteristics = new HashMap<>();

    @Transient
    private Map<String, Object> additionalInformation = new HashMap<>();

    @PrePersist
    @PreUpdate
    private void beforeSave() throws Exception {
        if (mainInfo != null) {
            mainInfoJson = objectMapper.writeValueAsString(mainInfo);
        }
        if (mainCharacteristics != null) {
            mainCharacteristicsJson = objectMapper.writeValueAsString(mainCharacteristics);
        }
        if (additionalInformation != null) {
            additionalInformationJson = objectMapper.writeValueAsString(additionalInformation);
        }
    }

    @PostLoad
    private void afterLoad() throws Exception {
        if (mainInfoJson != null) {
            mainInfo = objectMapper.readValue(mainInfoJson, Map.class);
        }
        if (mainCharacteristicsJson != null) {
            mainCharacteristics = objectMapper.readValue(mainCharacteristicsJson, Map.class);
        }
        if (additionalInformationJson != null) {
            additionalInformation = objectMapper.readValue(additionalInformationJson, Map.class);
        }
    }
}
