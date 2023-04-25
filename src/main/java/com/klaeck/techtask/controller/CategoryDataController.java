package com.klaeck.techtask.controller;

import com.klaeck.techtask.dto.CategoryDto;
import com.klaeck.techtask.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryDataController {
    @Autowired
    private CategoryService service;

    @PutMapping
    public int createCategory(@RequestBody CategoryDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping("/{id}")
    public void updateCategory(@PathVariable(name = "id") int id, @RequestBody CategoryDto dto) {
        service.update(id, dto);
    }

    @GetMapping("/{id}")
    public CategoryDto readCategory(@PathVariable int id) {
        return service.read(id);
    }
}
