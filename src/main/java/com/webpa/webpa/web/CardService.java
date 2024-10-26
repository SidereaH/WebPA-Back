package com.webpa.webpa.web;

import com.webpa.webpa.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private List<Card> cards = new ArrayList<>(); // Пример хранилища

    public List<Card> findAll() {
        return cards;
    }

    public Optional<Card> findById(Long id) {
        return cards.stream().filter(card -> card.getId().equals(id)).findFirst();
    }

    public Card save(Card card) {
        cards.add(card);
        return card;
    }

    public Card update(Card card) {
        delete(card.getId());
        cards.add(card);
        return card;
    }

    public void delete(Long id) {
        cards.removeIf(card -> card.getId().equals(id));
    }
}