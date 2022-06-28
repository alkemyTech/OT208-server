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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
class SlideControllerTest {

    String id;
    OrganizationEntity ong;
    SlideEntity slide;
    ObjectMapper objectMapper;
    ObjectWriter objectWriter;

    @MockBean
    public SlideService slideService;

    @Autowired
    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        id = "111111111111111111111111";
        ong = new OrganizationEntity("o123n123g123",
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
        slide = new SlideEntity(id,
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
        verify(slideService).createSlide(dtoRequest);
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
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getAll200() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        Mockito.when(slideService.getAll()).thenReturn(List.of(dtoResponse));
        mockMvc.perform(get("/slides"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
        verify(slideService).getAll();
    }


    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getSlide() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        Mockito.when(slideService.getSlide(dtoResponse.getId())).thenReturn(dtoResponse);
        mockMvc.perform(get("/slides/" + dtoResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dtoResponse)))
                .andDo(MockMvcResultHandlers.print());
        verify(slideService).getSlide(dtoResponse.getId());
    }


    @Test
    void updateSlide() {
        
    }


    // DELETE SLIDES
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testDeleteSlides204() throws Exception {

        when(slideService.deleteSlide(id)).thenReturn(true);
        mockMvc.perform(delete("/slides/" + id))
                .andExpect(status().isNoContent());
        verify(slideService).deleteSlide(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testDeleteSlides404() throws Exception {
        id = "test";
        when(slideService.deleteSlide(id)).thenReturn(false);
        mockMvc.perform(delete("/testimonials/test"))
                .andExpect(status().isNotFound());
        verify(slideService,never()).deleteSlide("test");

    }
}