package com.klaeck.techtask.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/card")
class CardDataController {
    @Autowired
    private CardService service;

    @PutMapping
    private int createCard(@RequestBody CardRequestDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    private void deleteCard(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping("/{id}")
    private void updateCard(@PathVariable(name = "id") int id,
                           @RequestBody CardRequestDto dto) {
        service.update(id, dto);
    }

    @PostMapping("/{id}/addCategories")
    private void addCategoriesToCard(@PathVariable(name = "id") int id,
                                    @RequestBody CardRequestDto dto) {
        service.addCategories(id, dto);
    }

    @GetMapping("/{id}")
    private CardResponseDto readCard(@PathVariable int id) {
        return service.read(id);
    }
}
