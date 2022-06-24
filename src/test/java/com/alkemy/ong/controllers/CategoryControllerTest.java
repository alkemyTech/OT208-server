package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.exeptions.ArgumentRequiredException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;


import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {WebSecurityConfiguration.class, JwtTokenFilter.class})},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AWSS3Service awss3Service;

    String id;

    CategoryEntity categoryEntity;

    ObjectMapper  objectMapper;

    @BeforeEach
    void setup(){
        id = "123456";
        categoryEntity = new CategoryEntity(id,"Categoria uno","david eduardo","http:/aImage.jpg", LocalDateTime.now(),false);

        objectMapper = new ObjectMapper();
        DateFormat dateFormat = objectMapper.getDateFormat();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(dateFormat);

    }

    @Test
    void getCategoryResponse200()throws Exception{
       List<CategoryBasicDto> dto = ObjectMapperUtils.mapAll(categoryService.findAll(),CategoryBasicDto.class);
       when(categoryService.getCategoriesDto()).thenReturn(dto);
       mockMvc.perform(get("/categories"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(dto)))
               .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getCategoryResponse404()throws Exception{
        List<CategoryBasicDto> dto = null;
        when(categoryService.getCategoriesDto()).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/categories"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createCategoryWhitImageReturnCategoryDetailDtoStatus201()throws Exception {
        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity,EntryCategoryDto.class);
        CategoryDetailDto categoryDetailDto = ObjectMapperUtils.map(categoryEntity,CategoryDetailDto.class);
        MockMultipartFile mockMultiparEntryDto = new MockMultipartFile("category","category.json","application/json",
                objectMapper.writeValueAsString(entryCategoryDto).getBytes());
        MockMultipartFile multipartImage = new MockMultipartFile("file","miImagen.jpg","image/jpeg",
                "miImagen.jpg".getBytes());

        when(awss3Service.uploadFile(multipartImage)).thenReturn("miImagen.jpg");
        when(categoryService.save(any())).thenReturn(categoryEntity);

        mockMvc.perform(multipart("/categories").file(mockMultiparEntryDto).file(multipartImage))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDetailDto),true));

        verify(awss3Service).uploadFile(multipartImage);
        verify(categoryService).save(any());
    }

    @Test
    void update_doesNotExistById_ReturnStatus404()throws Exception{
        EntryCategoryDto entryCategoryDto = ObjectMapperUtils.map(categoryEntity,EntryCategoryDto.class);
        String jsonUserRegister = objectMapper.writeValueAsString(entryCategoryDto);

        id="12";

        when(categoryService.editCategory(id,entryCategoryDto,null)).thenThrow(ArgumentRequiredException.class);

        mockMvc.perform(put("/categories/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonUserRegister))
                .andExpect(status().isNotFound());

        verify(categoryService).editCategory(id,entryCategoryDto,null);
    }


}
