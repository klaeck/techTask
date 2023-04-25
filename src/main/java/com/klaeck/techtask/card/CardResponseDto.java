package com.klaeck.techtask.card;

import com.klaeck.techtask.category.CategoryDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
class CardResponseDto {
    private String name;
    private List<CategoryDto> categories;

    public CardResponseDto(Card card) {
        this.name = card.getName();
        this.categories = card.getCategories().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }
}
