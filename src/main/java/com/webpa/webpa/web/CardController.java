package com.webpa.webpa.web;


import com.webpa.webpa.Card;
import com.webpa.webpa.Category;
import com.webpa.webpa.Partner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/cards", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class CardController {

    private final List<Card> cards = new ArrayList<>();

    // Конструктор для создания заранее определенных карточек
    public CardController() {
        // Пример создания карточек
        cards.add(new Card(1L, new Date(), "Card 1", "Description 1", new Category(1, "Category 1"), true, "USA", new Date(), new Partner(1, "Partner 1"), "Red", "Articul 1", "http://example.com/image1.jpg"));
        cards.add(new Card(2L, new Date(), "Card 2", "Description 2", new Category(2, "Category 2"), true, "Canada", new Date(), new Partner(2, "Partner 2"), "Blue", "Articul 2", "http://example.com/image2.jpg"));
        // Добавьте больше карточек по необходимости
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cards; // Возвращает список заранее созданных карточек
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return cards.stream()
                .filter(card -> card.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}