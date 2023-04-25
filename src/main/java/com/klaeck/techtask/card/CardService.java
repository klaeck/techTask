package com.klaeck.techtask.card;

import com.klaeck.techtask.category.Category;
import com.klaeck.techtask.category.CategoryRepo;
import com.klaeck.techtask.exception.NotFoundException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class CardService {
    private final CardRepo cardRepo;
    private final CategoryRepo categoryRepo;
    private final SimpleJdbcInsert jdbcInsert;

    CardService(CardRepo cardRepo, CategoryRepo categoryRepo, DataSource dataSource) {
        this.cardRepo = cardRepo;
        this.categoryRepo = categoryRepo;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("card_to_category");
    }

    @Transactional(readOnly = true)
    CardResponseDto read(int id) {
        Card card = cardRepo.findById(id).orElseThrow(NotFoundException::new);
        return new CardResponseDto(card);
    }

    @Transactional
    void update(int id, CardRequestDto update) {
        Card card = cardRepo.findById(id).orElseThrow(NotFoundException::new);

        if (update.getName() != null) {
            card.setName(update.getName());
        }

        if (update.getCategoryIds() != null) {
            cardRepo.deleteAllCardCategories(id);
            insertCardToCategoryDependencies(id, update.getCategoryIds());
        }
    }

    @Transactional
    int create(CardRequestDto dto) {
        int cardId = cardRepo.save(new Card(dto.getName())).getId();

        List<Integer> categoriesToInsert = new ArrayList<>();

        if (dto.getCategoryIds() != null) {
            categoriesToInsert.addAll(dto.getCategoryIds());
        }

        if (dto.getNewCategories() != null) {
            List<Category> newCategories = categoryRepo.saveAll(dto.getNewCategories().stream()
                    .map(categoryDto -> new Category(categoryDto.getName()))
                    .collect(Collectors.toList()));

            categoriesToInsert.addAll(newCategories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList()));
        }

        insertCardToCategoryDependencies(cardId, categoriesToInsert);

        return cardId;
    }

    @SuppressWarnings("unchecked")
    void insertCardToCategoryDependencies(int cardId, List<Integer> categoryIds) {
        Map<String, Integer>[] dataToInsert = categoryIds.stream()
                .map(categoryId -> {
                    Map<String, Integer> map = new HashMap<>(1);

                    map.put("card_id", cardId);
                    map.put("category_id", categoryId);

                    return map;
                })
                .toArray(Map[]::new);

        jdbcInsert.executeBatch(dataToInsert);
    }

    @Transactional
    void delete(int id) {
        cardRepo.deleteById(id);
    }

    @Transactional
    void addCategories(int cardId, CardRequestDto dto) {
        if (dto.getCategoryIds() == null) {
            return;
        }

        insertCardToCategoryDependencies(cardId, dto.getCategoryIds());
    }
}
