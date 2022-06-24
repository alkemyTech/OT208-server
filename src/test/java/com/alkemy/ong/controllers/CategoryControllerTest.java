package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.exeptions.ArgumentRequiredException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {WebSecurityConfiguration.class, JwtTokenFilter.class})},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CategoryControllerTest {

    String id;
    CategoryEntity categoryEntity;
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private AWSS3Service awss3Service;

    @BeforeEach
    void setup() {
        id = "123456";
        categoryEntity = new CategoryEntity(id, "Test Category", "Test Description", "http:/aImage.jpg", LocalDateTime.now(), false);
        objectMapper = new ObjectMapper();
        DateFormat df = objectMapper.getDateFormat();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(df);
    }

    @Test
    void getCategoryResponse200() throws Exception {
        Page<CategoryBasicDto> dto = Mockito.mock(Page.class);
        when(categoryService.getCategories(any(Pageable.class))).thenReturn(dto);
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(categoryService).getCategories(any(Pageable.class));
    }

    @Test
    void getCategoryResponse404()throws Exception{
        Page<CategoryBasicDto> dto = Mockito.mock(Page.class);
        when(categoryService.getCategories(any(Pageable.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/categories"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createCategoryWhitImage201() throws Exception {

        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);

        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category", "category.json", "application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile("img", "image.jpg", "image/jpeg", "image".getBytes());

        when(categoryService.saveCategory(entryCategoryDto,multipartImage)).thenReturn(categoryDetailDto);
        mockMvc.perform(multipart("/categories").file(mockMultiparEntryDto).file(multipartImage))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDetailDto), true));
        verify(categoryService).saveCategory(entryCategoryDto,multipartImage);
    }

    @Test
    void createCategoryWhitoutImage201() throws Exception {

        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);

        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category", "category.json", "application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile(
                "img",
                null,
                "image/jpeg", new byte[0]);

        when(categoryService.saveCategory(any(),any())).thenReturn(categoryDetailDto);
        mockMvc.perform(multipart("/categories").file(mockMultiparEntryDto).file(multipartImage))
                .andExpect(status().isCreated());
        verify(categoryService).saveCategory(entryCategoryDto);
    }

    @Test
    void createCategoryValidation400() throws Exception {

        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);
        entryCategoryDto.setName("");//name is empty
        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category", "category.json", "application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile(
                "img",
                null,
                "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/categories").file(mockMultiparEntryDto).file(multipartImage))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());
        verify(categoryService,never()).saveCategory(entryCategoryDto,multipartImage);

    }

    @Test
    void updateReturn200() throws Exception {
        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);

        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category",
                "category.json",
                "application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile(
                "img",
                null,
                "image/jpeg", new byte[0]);
        when(categoryService.existById(id)).thenReturn(true);
        when(categoryService.editCategory(id, entryCategoryDto, multipartImage)).thenReturn(categoryDetailDto);
        mockMvc.perform(multipart("/categories/" + id).file(mockMultiparEntryDto).file(multipartImage)
                    .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDetailDto), true));
        verify(categoryService).editCategory(id, entryCategoryDto, multipartImage);
    }

    @Test
    void update_doesNotExistById_ReturnStatus404() throws Exception {
        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category", "category.json", "application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile(
                "img",
                null,
                "image/jpeg", new byte[0]);
        id = "any";//id is not exist

        mockMvc.perform(multipart("/categories/" + id).file(mockMultiparEntryDto).file(multipartImage)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isNotFound());
        verify(categoryService,never()).editCategory(id, entryCategoryDto, multipartImage);
    }

    @Test
    void updateValidation400() throws Exception {
        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);
        entryCategoryDto.setName("");//name is empty
        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category",
                "category.json",
                "application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile(
                "img",
                null,
                "image/jpeg", new byte[0]);
        when(categoryService.existById(id)).thenReturn(true);
        when(categoryService.editCategory(id, entryCategoryDto, multipartImage)).thenReturn(categoryDetailDto);
        mockMvc.perform(multipart("/categories/" + id).file(mockMultiparEntryDto).file(multipartImage)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());
        verify(categoryService,never()).editCategory(id, entryCategoryDto, multipartImage);
    }

    @Test
    void deleteReturn204() throws Exception {
        when(categoryService.existById(id)).thenReturn(true);
        mockMvc.perform(delete("/categories/" + id))
                .andExpect(status().isNoContent());
        verify(categoryService).deleteById(id);
    }

    @Test
    void deleteReturn404() throws Exception {
        id = "any";//id is not exist
        when(categoryService.existById(id)).thenReturn(false);
        mockMvc.perform(delete("/categories/" + id))
                .andExpect(status().isNotFound());
        verify(categoryService,never()).deleteById(id);
    }

    @Test
    void getIdReturn200() throws Exception {
        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity, EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);

        when(categoryService.getDetailDto(id)).thenReturn(categoryDetailDto);
        mockMvc.perform(get("/categories/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDetailDto), true));
        verify(categoryService).getDetailDto(id);
    }

    @Test
    void getIdReturn404() throws Exception {
        id = "any";//id is not exist
        when(categoryService.getDetailDto(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/categories/" + id))
                .andExpect(status().isNotFound());
        verify(categoryService).getDetailDto(id);
    }
}
