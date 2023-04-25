package com.klaeck.techtask.repository;

import com.klaeck.techtask.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
