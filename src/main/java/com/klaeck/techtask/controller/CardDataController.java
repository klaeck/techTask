package com.klaeck.techtask.controller;

import com.klaeck.techtask.dto.CardRequestDto;
import com.klaeck.techtask.dto.CardResponseDto;
import com.klaeck.techtask.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/card")
public class CardDataController {
    @Autowired
    private CardService service;

    @PutMapping
    public int createCard(@RequestBody CardRequestDto cardRequestDto) {
        return service.create(cardRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping("/{id}")
    public void updateCard(@PathVariable(name = "id") int idToUpd,
                           @RequestBody CardRequestDto cardRequestDto) {
        service.update(idToUpd, cardRequestDto);
    }

    @PostMapping("/{id}/addCategories")
    public void addCategoriesToCard(@PathVariable(name = "id") int idToUpd,
                                    @RequestBody CardRequestDto cardRequestDto) {
        service.addCategories(idToUpd, cardRequestDto);
    }

    @GetMapping("/{id}")
    public CardResponseDto readCard(@PathVariable int id) {
        return service.read(id);
    }
}
