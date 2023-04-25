package com.klaeck.techtask.dto;


import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

@Data
public class CardRequestDto {
    @Min(value = 1, message = "Name of the card should be at least 1 character long")
    String name;

    private List<Integer> categoryIds;

    private List<CategoryDto> newCategories;
}
