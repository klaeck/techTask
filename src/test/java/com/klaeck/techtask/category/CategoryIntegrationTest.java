package com.klaeck.techtask.category;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepo categoryRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testCategorySaving() throws Exception {
        // given
        String name = "TestCategory";
        CategoryDto dto = createCategoryDto(name);

        // when
        MvcResult result = mockMvc.perform(put("/category")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        entityManager.clear();

        int categoryId = Integer.parseInt(result.getResponse().getContentAsString());

        Category category = categoryRepo.findById(categoryId).orElseThrow(AssertionFailedError::new);

        assertEquals(name, category.getName());
    }

    private CategoryDto createCategoryDto(String name) {
        CategoryDto dto = new CategoryDto();
        dto.setName(name);
        return dto;
    }

    @Test
    public void testUpdateWithValidInput() throws Exception {
        // given
        String originalName = "TestCategory";
        String updatedName = "UpdateTestCategory";

        CategoryDto updatedDto = createCategoryDto(updatedName);

        // when
        int id = categoryRepo.save(new Category(originalName)).getId();

        mockMvc.perform(post("/category/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk());
        // then
        entityManager.flush();
        entityManager.clear();

        Category category = categoryRepo.findById(id).orElseThrow(AssertionFailedError::new);

        assertEquals(updatedName, category.getName());
    }

    @Test
    public void testUpdateWithoutChanges() throws Exception {
        // given
        String originalName = "TestCategory";

        CategoryDto updatedDto = createCategoryDto(null);

        // when
        int id = categoryRepo.save(new Category(originalName)).getId();

        mockMvc.perform(post("/category/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk());
        // then
        entityManager.clear();

        Category category = categoryRepo.findById(id).orElseThrow(AssertionFailedError::new);

        assertEquals(originalName, category.getName());
    }

    @Test
    public void testDelete() throws Exception {
        String name = "TestCard";

        //when
        int id = categoryRepo.save(new Category(name)).getId();

        mockMvc.perform(delete("/category/" + id)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        // then
        entityManager.clear();
        assertFalse(categoryRepo.findAll().stream()
                .noneMatch(category -> category.getId() == id));
    }
}
