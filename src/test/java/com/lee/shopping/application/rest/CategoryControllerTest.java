package com.lee.shopping.application.rest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getCategoriesLowest() throws Exception {
        // when
        mockMvc.perform(get("/categories/lowest")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // JSON 구조 검증
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories[0].category").exists())
                .andExpect(jsonPath("$.categories[0].brand").exists())
                .andExpect(jsonPath("$.categories[0].price").exists())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andReturn();


    }

    @Test
    void getCategoryLowestHighest() throws Exception {
        String categoryId = "상의"; // 존재하는 카테고리

        // when
        mockMvc.perform(get("/categories/{categoryId}/lowest-highest", categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // JSON 구조 검증
                .andExpect(jsonPath("$.category").value(categoryId))
                .andExpect(jsonPath("$.lowest").isArray())
                .andExpect(jsonPath("$.lowest[0].brand").exists())
                .andExpect(jsonPath("$.lowest[0].price").exists())
                .andExpect(jsonPath("$.highest").isArray())
                .andExpect(jsonPath("$.highest[0].brand").exists())
                .andExpect(jsonPath("$.highest[0].price").exists())
                .andReturn();


    }

    @Test
    void getCategoryLowestHighestFail() throws Exception {
        String categoryId = "없는카테고리"; // 존재하는 카테고리

        // when
        mockMvc.perform(get("/categories/{categoryId}/lowest-highest", categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }
}