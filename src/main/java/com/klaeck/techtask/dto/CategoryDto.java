package com.klaeck.techtask.dto;

import com.klaeck.techtask.entity.Category;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CategoryDto {
    @Min(value = 1, message = "Name of the category should be at least 1 character long")
    private String name;

    public CategoryDto() {}

    public CategoryDto(Category category) {
        this.name = category.getName();
    }
}
