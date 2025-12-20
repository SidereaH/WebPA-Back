package com.webpa.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class AttributeNormalizer {

    private static final Map<String, String> SYNONYM_TO_KEY = Map.ofEntries(
            Map.entry("артикул", "sku"),
            Map.entry("id", "sku"),
            Map.entry("код товара", "sku"),
            Map.entry("бренд", "brand"),
            Map.entry("производитель", "brand"),
            Map.entry("страна", "country"),
            Map.entry("страна производства", "country"),
            Map.entry("количество", "count"),
            Map.entry("количество капсул/таблеток", "count"),
            Map.entry("вес", "weight"),
            Map.entry("масса", "weight"),
            Map.entry("объем", "volume"),
            Map.entry("объём", "volume"),
            Map.entry("цвет", "color"),
            Map.entry("размер", "size")
    );

    public Map<String, String> normalize(Map<String, String> raw) {
        if (raw == null || raw.isEmpty()) {
            return Map.of();
        }
        Map<String, String> result = new HashMap<>();
        raw.forEach((key, value) -> {
            if (value == null || key == null) {
                return;
            }
            String cleanedKey = key.trim().toLowerCase(Locale.ROOT);
            String canonical = SYNONYM_TO_KEY.getOrDefault(cleanedKey, cleanedKey);
            result.put(canonical, value.trim());
        });
        return result;
    }
}
