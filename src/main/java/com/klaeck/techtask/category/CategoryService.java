package com.klaeck.techtask.category;

import com.klaeck.techtask.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CategoryService {
    private final CategoryRepo repo;

    CategoryService(CategoryRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    CategoryDto read(int id) {
        Category category = repo.findById(id).orElseThrow(NotFoundException::new);

        return new CategoryDto(category);
    }

    @Transactional
    void update(int id, CategoryDto dto) {
        if (dto.getName() == null) {
            return;
        }

        Category category = repo.findById(id).orElseThrow(NotFoundException::new);
        category.setName(dto.getName());
    }

    @Transactional
    void delete(int id) {
        repo.deleteById(id);
    }

    @Transactional
    int create(CategoryDto dto) {
        return repo.save(new Category(dto.getName())).getId();
    }
}
