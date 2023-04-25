package com.klaeck.techtask.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
class CategoryDataController {
    @Autowired
    private CategoryService service;

    @PutMapping
    private int createCategory(@RequestBody CategoryDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    private void deleteCategory(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping("/{id}")
    private void updateCategory(@PathVariable(name = "id") int id, @RequestBody CategoryDto dto) {
        service.update(id, dto);
    }

    @GetMapping("/{id}")
    private CategoryDto readCategory(@PathVariable int id) {
        return service.read(id);
    }
}
