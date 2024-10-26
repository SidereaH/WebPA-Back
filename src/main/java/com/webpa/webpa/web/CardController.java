package com.webpa.webpa.web;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class CardController {
    @RestController
    @RequestMapping(path = "/api/cards", produces = "application/json")
    @CrossOrigin(origins = "http://localhost:8080")
    public class TacoController {
        // private TacoRepository tacoRepo;
        // public TacoController(TacoRepository tacoRepo) {
        //     this.tacoRepo = tacoRepo;
        // }

        // @GetMapping(params="recent")
        // public Iterable<Taco> recentTacos() {
        //     PageRequest page = PageRequest.of(
        //             0, 12, Sort.by("createdAt").descending());
        //     return tacoRepo.findAll(page).getContent();
        // }         
    }
}
