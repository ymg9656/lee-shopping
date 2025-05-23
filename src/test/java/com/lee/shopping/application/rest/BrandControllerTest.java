package com.lee.shopping.application.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.shopping.application.request.BrandRequest;
import com.lee.shopping.application.response.TestBrandCategoryPrices;
import com.lee.shopping.application.response.TestBrandSetLowestResponse;
import com.lee.shopping.application.response.TestCategoryPrice;
import com.lee.shopping.application.response.TestProductResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBrandCategoriesLowest() throws Exception {

        MvcResult result = mockMvc.perform(get("/brands/categories/lowest")
                        .accept(MediaType.APPLICATION_JSON))
                // 1. 상태 코드 검증
                .andExpect(status().isOk())

                // 2. Content-Type 검증
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // 3. JSON 스키마/필드 존재 여부 예시 (예: name, categories 필드 존재 확인)
                .andExpect(jsonPath("$.lowest").exists())
                .andExpect(jsonPath("$.lowest.brand").exists())

                // 4. 데이터 개수 검증 (categories가 배열이고 최소 1개 이상)
                .andExpect(jsonPath("$.lowest.categories").isArray())
                .andReturn();


        TestBrandSetLowestResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), TestBrandSetLowestResponse.class);
        TestBrandCategoryPrices brandCategoryPrices = response.getLowest();


        String totalPriceStr = brandCategoryPrices.getTotalPrice();
        long totalPrice = parsePrice(totalPriceStr);

        List<TestCategoryPrice> categoryPrices = brandCategoryPrices.getCategories();
        long sumCategoriesPrice = 0;
        for (TestCategoryPrice category : categoryPrices) {
            sumCategoriesPrice += parsePrice(category.getPrice());
        }
        //5. 합계가 같은지 확인
        assertThat(sumCategoriesPrice).isEqualTo(totalPrice);
    }


    @Test
    void registerSuccess() throws Exception {

        BrandRequest request = BrandRequest.builder()
                .brand("Nike")
                .build();

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void registerFailBlank() throws Exception {
        BrandRequest request = BrandRequest.builder()
                .brand("") // NotBlank 위반
                .build();

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerFailMaxSize() throws Exception {
        BrandRequest request = BrandRequest.builder()
                .brand("12345678911") // NotBlank 위반
                .build();

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerFailDuplicate() throws Exception {
        BrandRequest request = BrandRequest.builder()
                .brand("A") // 중복 등록 위반
                .build();

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSuccess() throws Exception {
        // 먼저 브랜드 등록
        BrandRequest request = BrandRequest.builder()
                .brand("Adidas")
                .build();

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // 브랜드 삭제
        mockMvc.perform(delete("/brands/Adidas"))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteFail() throws Exception {
        //없는 브랜드 삭제 요청.
        mockMvc.perform(delete("/brands/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private long parsePrice(String priceWithComma) {
        return Long.parseLong(priceWithComma.replaceAll(",", ""));
    }
}