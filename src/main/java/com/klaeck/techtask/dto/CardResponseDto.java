package com.klaeck.techtask.dto;

import com.klaeck.techtask.entity.Card;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CardResponseDto {
    private String name;
    private List<CategoryDto> categories;

    public CardResponseDto(Card card) {
        this.name = card.getName();
        this.categories = card.getCategories().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }
}
