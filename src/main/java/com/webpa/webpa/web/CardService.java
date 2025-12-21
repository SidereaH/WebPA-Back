package com.webpa.webpa.web;

import com.webpa.webpa.ProductCard;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private List<ProductCard> cards = new ArrayList<>(); // Пример хранилища

    public List<ProductCard> findAll() {
        return cards;
    }

    public Optional<ProductCard> findById(Long id) {
        return cards.stream().filter(card -> card.getId().equals(id)).findFirst();
    }

    public ProductCard save(ProductCard card) {
        cards.add(card);
        return card;
    }

    public ProductCard update(ProductCard card) {
        delete(card.getId());
        cards.add(card);
        return card;
    }

    public void delete(Long id) {
        cards.removeIf(card -> card.getId().equals(id));
    }
}