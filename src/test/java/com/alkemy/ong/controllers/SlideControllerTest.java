package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.slide.EntrySlideDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.models.SlideEntity;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
class SlideControllerTest {

    @MockBean
    public SlideService slideService;
    @Autowired
    public MockMvc mockMvc;
    String id;
    OrganizationEntity ong;
    SlideEntity slide;
    ObjectMapper objectMapper;
    ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        id = "o123n123g123";
        ong = new OrganizationEntity(id,
                "theName",
                "http:/aImage.jpg",
                null,
                null,
                "ong@mail.com",
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...",
                null,
                null,
                null,
                null, LocalDateTime.now(),
                false);
        slide = new SlideEntity("1111111",
                "http:/anotherImage1.jpg",
                "one text",
                1, ong);
        objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void createSlide201() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        EntrySlideDto dtoRequest = ObjectMapperUtils.map(dtoResponse, EntrySlideDto.class);
        String content = objectWriter.writeValueAsString(dtoRequest);
        Mockito.when(slideService.createSlide(dtoRequest)).thenReturn(dtoResponse);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/slides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void createSlide400() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        EntrySlideDto dtoRequest = ObjectMapperUtils.map(dtoResponse, EntrySlideDto.class);
        dtoRequest.setText(null);
        String content = objectWriter.writeValueAsString(dtoRequest);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/slides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAll() {
    }

    @Test
    void getSlide() {
    }

    @Test
    void updateSlide() {
    }

    @Test
    void deleteSlide() {
    }
}