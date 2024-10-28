package com.webpa.webpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "product_card")
@Data
public class ProductCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String name;

    @Positive
    private Integer price;

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
    
    @Column(columnDefinition = "TEXT")
    private String marketplace;
    @Column(columnDefinition = "TEXT")
    private String url;

}
