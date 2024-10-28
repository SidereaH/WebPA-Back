package com.webpa.webpa;

import com.webpa.webpa.ProductCard;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductAnalyzer {

    // Метод для предварительной обработки данных
    public List<ProductCard> preprocessData(List<ProductCard> productCards, String searchQuery) {
        // Нормализация данных
        normalizeData(productCards);

        // Автоматическое дополнение информации
        enrichData(productCards, searchQuery);

        return productCards;
    }

    // Метод для нормализации данных
    private void normalizeData(List<ProductCard> productCards) {
        // Пример: Приведение названий брендов к единому виду
        Map<String, String> brandMapping = new HashMap<>();
        brandMapping.put("Nike", "Nike");
        brandMapping.put("NIKE", "Nike");
        brandMapping.put("Adidas", "Adidas");
        brandMapping.put("adidas", "Adidas");

        for (ProductCard productCard : productCards) {
            String brand = (String) productCard.getMainInfo().get("brand");
            if (brandMapping.containsKey(brand)) {
                productCard.getMainInfo().put("brand", brandMapping.get(brand));
            }
        }
    }

    // Метод для автоматического дополнения информации
    private void enrichData(List<ProductCard> productCards, String searchQuery) {
        Date currentDate = new Date();

        for (ProductCard productCard : productCards) {
            // Добавление даты и названия запроса
            productCard.getAdditionalInformation().put("searchDate", currentDate);
            productCard.getAdditionalInformation().put("searchQuery", searchQuery);

            // Добавление комментариев
            if (productCard.getPrice() < 1000) {
                productCard.getAdditionalInformation().put("comment", "Low price");
            } else if (productCard.getPrice() > 5000) {
                productCard.getAdditionalInformation().put("comment", "High price");
            }
        }
    }

    // Метод для вывода анализа
    public void printAnalysis(List<ProductCard> productCards) {
        // Группировка товаров по брендам
        Map<String, List<ProductCard>> brandGroups = productCards.stream()
                .collect(Collectors.groupingBy(card -> (String) card.getMainInfo().get("brand")));

        System.out.println("Product Analysis:");
        for (Map.Entry<String, List<ProductCard>> entry : brandGroups.entrySet()) {
            String brand = entry.getKey();
            List<ProductCard> brandProducts = entry.getValue();

            System.out.println("Brand: " + brand);
            System.out.println("Total Products: " + brandProducts.size());

            // Вывод средней цены
            double averagePrice = brandProducts.stream()
                    .mapToDouble(ProductCard::getPrice)
                    .average()
                    .orElse(0.0);
            System.out.println("Average Price: " + averagePrice);

            // Вывод среднего рейтинга
            double averageRating = brandProducts.stream()
                    .mapToDouble(card -> {
                        Double supplierRating = (Double) card.getAdditionalInformation().get("supplierRating");
                        return supplierRating != null ? supplierRating : 0.0;
                    })
                    .average()
                    .orElse(0.0);
            System.out.println("Average Rating: " + averageRating);

            // Вывод комментариев
            brandProducts.forEach(card -> {
                System.out.println("Product: " + card.getName());
                System.out.println("Price: " + card.getPrice());
                System.out.println("Rating: " + card.getAdditionalInformation().get("supplierRating"));
                System.out.println("Comment: " + card.getAdditionalInformation().get("comment"));
                System.out.println("-----------------------------");
            });
        }
    }
}