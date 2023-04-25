package com.klaeck.techtask.card;


import com.klaeck.techtask.category.CategoryDto;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

@Data
class CardRequestDto {
    @Min(value = 1, message = "Name of the card should be at least 1 character long")
    private String name;

    private List<Integer> categoryIds;

    private List<CategoryDto> newCategories;
}
