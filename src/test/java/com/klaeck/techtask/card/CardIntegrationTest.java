package com.klaeck.techtask.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaeck.techtask.category.Category;
import com.klaeck.techtask.category.CategoryRepo;
import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CardIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    WebApplicationContext context;

    private List<Integer> testCategoriesIds;

    @Before
    public void setUp() {
        Date currentDate = new Date();
        List<Category> testCategories = List.of(new Category("testCat1" + currentDate),
                new Category("testCat2" + currentDate), new Category("testCat3" + currentDate));

        testCategoriesIds = categoryRepo.saveAll(testCategories).stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testCardSaving() throws Exception {
        // given
        String name = "Test";
        CardRequestDto dto = createCardRequestDto(name, testCategoriesIds);

        // when
        MvcResult result = mockMvc.perform(put("/card")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        entityManager.clear();

        int cardId = Integer.parseInt(result.getResponse().getContentAsString());

        Card card = cardRepo.findById(cardId).orElseThrow(AssertionFailedError::new);

        assertEquals(name, card.getName());
        assertEquals(testCategoriesIds, card.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    private CardRequestDto createCardRequestDto(String name, List<Integer> categoriesIds) {
        CardRequestDto dto = new CardRequestDto();
        dto.setName(name);
        dto.setCategoryIds(categoriesIds);
        return dto;
    }

    @Test
    public void testUpdateAll() throws Exception {
        // given
        String originalName = "FullValid";
        String updatedName = "StillFullValid";
        List<Integer> reducedCategories = List.of(testCategoriesIds.get(0));

        CardRequestDto originalDto = createCardRequestDto(originalName, testCategoriesIds);
        CardRequestDto updatedDto = createCardRequestDto(updatedName, reducedCategories);

        //when
        int cardId = cardService.create(originalDto);
        mockMvc.perform(post("/card/" + cardId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk());

        // then
        entityManager.clear();

        Card card = cardRepo.findById(cardId).orElseThrow(AssertionFailedError::new);

        assertEquals(updatedName, card.getName());
        assertEquals(reducedCategories, card.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void testUpdateNameOnly() throws Exception {
        String originalName = "FullValid";
        String updatedName = "UpdatedFullValid";

        CardRequestDto originalDto = createCardRequestDto(originalName, testCategoriesIds);
        CardRequestDto updatedDto = createCardRequestDto(updatedName, null);

        //when
        int cardId = cardService.create(originalDto);
        MvcResult result = mockMvc.perform(post("/card/" + cardId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        entityManager.flush();
        entityManager.clear();

        Card card = cardRepo.findById(cardId).orElseThrow(AssertionFailedError::new);

        assertEquals(updatedName, card.getName());
        assertEquals(testCategoriesIds, card.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void testUpdateCategoriesOnly() throws Exception {
        String originalName = "FullValid";
        List<Integer> reducedCategories = List.of(testCategoriesIds.get(0));

        CardRequestDto originalDto = createCardRequestDto(originalName, testCategoriesIds);
        CardRequestDto updatedDto = createCardRequestDto(null, reducedCategories);

        //when
        int cardId = cardService.create(originalDto);
        MvcResult result = mockMvc.perform(post("/card/" + cardId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        entityManager.clear();

        Card card = cardRepo.findById(cardId).orElseThrow(AssertionFailedError::new);

        assertEquals(originalName, card.getName());
        assertEquals(reducedCategories, card.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void testUpdateWithoutChanges() throws Exception {
        String originalName = "FullValid";

        CardRequestDto originalDto = createCardRequestDto(originalName, testCategoriesIds);
        CardRequestDto updatedDto = createCardRequestDto(null, null);

        //when
        int cardId = cardService.create(originalDto);
        MvcResult result = mockMvc.perform(post("/card/" + cardId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        entityManager.clear();

        Card card = cardRepo.findById(cardId).orElseThrow(AssertionFailedError::new);

        assertEquals(originalName, card.getName());
        assertEquals(testCategoriesIds, card.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void testAddCategories() throws Exception {
        // given
        String name = "FullValid";
        List<Integer> firstCategory = List.of(testCategoriesIds.get(0));

        CardRequestDto dto = createCardRequestDto(name, firstCategory);
        CardRequestDto updatedDto = createCardRequestDto(null, testCategoriesIds.subList(1, 3));

        //when
        int cardId = cardService.create(dto);
        mockMvc.perform(post("/card/" + cardId + "/addCategories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk());

        // then
        entityManager.clear();

        Card card = cardRepo.findById(cardId).orElseThrow(AssertionFailedError::new);

        assertEquals(name, card.getName());
        assertEquals(testCategoriesIds, card.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void testDelete() throws Exception {
        String originalName = "TestCard";

        CardRequestDto dto = createCardRequestDto(originalName, testCategoriesIds);

        //when
        int cardId = cardService.create(dto);
        mockMvc.perform(delete("/card/" + cardId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        // then
        entityManager.clear();
        assertFalse(cardRepo.findAll().stream()
                .noneMatch(card -> card.getId() == cardId));
    }
}
