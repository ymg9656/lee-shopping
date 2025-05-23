package com.lee.shopping.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.TestProductResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerSuccess() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(1234)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void registerFailBrandBlank() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("")
                .category("상의")
                .price(1234)
                .build();
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerFailBrandNotFound() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("없는브랜드")
                .category("상의")
                .price(1234)
                .build();
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerFailCategoryNotFound() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("없는카테고리")
                .price(1234)
                .build();
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void registerFailPriceMinValid() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(0)
                .build();
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerFailPriceMaxValid() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(100000001)
                .build();
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void modifySuccess() throws Exception {
        //given 먼저 등록
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(1234)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        TestProductResponse originalResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TestProductResponse.class);


        ProductRequest modifiyRequest = ProductRequest.builder()
                .brand(request.getBrand())
                .category(request.getCategory())
                .price(555)
                .build();

        mvcResult = mockMvc.perform(put("/products/" + originalResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiyRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();


        TestProductResponse updatedResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TestProductResponse.class);

        // then: 변경 전/후 비교
        assertThat(updatedResponse.getId()).isEqualTo(originalResponse.getId());
        assertThat(updatedResponse.getBrand()).isEqualTo(originalResponse.getBrand());
        assertThat(updatedResponse.getCategory()).isEqualTo(originalResponse.getCategory());
        assertThat(updatedResponse.getPrice()).isNotEqualTo(originalResponse.getPrice()); // 가격만 변경됨

    }
    @Test
    void modifyFailNotFoundBrand() throws Exception {
        //given 먼저 등록
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(1234)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        TestProductResponse originalResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TestProductResponse.class);


        ProductRequest modifiyRequest = ProductRequest.builder()
                .brand("없는브랜드")
                .category(request.getCategory())
                .price(555)
                .build();

        mockMvc.perform(put("/products/" + originalResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiyRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyFailNotFoundCategory() throws Exception {
        //given 먼저 등록
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(1234)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        TestProductResponse originalResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TestProductResponse.class);


        ProductRequest modifiyRequest = ProductRequest.builder()
                .brand(request.getBrand())
                .category("없는카테고리")
                .price(555)
                .build();

        mockMvc.perform(put("/products/" + originalResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiyRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyFailTypeMismatch() throws Exception {
        ProductRequest modifiyRequest = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(555)
                .build();

        mockMvc.perform(put("/products/아이디타입잘못")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiyRequest)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void modifyFailInvalidId() throws Exception {
        ProductRequest modifiyRequest = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(555)
                .build();

        mockMvc.perform(put("/products/9999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiyRequest)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void deleteSuccess() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .brand("A")
                .category("상의")
                .price(1234)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        TestProductResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TestProductResponse.class);



        mockMvc.perform(delete("/products/" + response.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFail() throws Exception {
        //없는 데이터 삭제 요청.
        mockMvc.perform(delete("/products/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}