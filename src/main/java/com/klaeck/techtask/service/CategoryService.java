package com.klaeck.techtask.service;

import com.klaeck.techtask.entity.Category;
import com.klaeck.techtask.dto.CategoryDto;
import com.klaeck.techtask.repository.CategoryRepo;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepo repo;

    public CategoryService(CategoryRepo repo) {
        this.repo = repo;
    }

    public CategoryDto read(int id) {
        Category category = repo.findById(id).orElseThrow(IllegalArgumentException::new);

        return new CategoryDto(category);
    }

    public void update(int id, CategoryDto category) {
        if (category.getName() == null) {
            return;
        }

        Category categoryToUpd = repo.findById(id).orElseThrow(IllegalArgumentException::new);
        categoryToUpd.setName(category.getName());
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

    public int create(CategoryDto category) {
        return repo.save(new Category(category.getName())).getId();
    }
}
