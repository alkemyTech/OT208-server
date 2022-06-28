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
import org.springframework.mock.web.MockMultipartFile;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void getSlide200() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        Mockito.when(slideService.existById(id)).thenReturn(true);
        Mockito.when(slideService.getSlide(dtoResponse.getId())).thenReturn(dtoResponse);
        mockMvc.perform(get("/slides/" + dtoResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dtoResponse)))
                .andDo(MockMvcResultHandlers.print());
        verify(slideService).getSlide(dtoResponse.getId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getSlide404() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        Mockito.when(slideService.existById("test")).thenReturn(false);
        mockMvc.perform(get("/slides/" + "test"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        verify(slideService, never()).getSlide(dtoResponse.getId());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateSlide200() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        MockMultipartFile multipartImg = new MockMultipartFile(
                "file",
                "imagen.jpg",
                "image/jpeg", "imagen.jpg".getBytes());

        when(slideService.existById(id)).thenReturn(true);
        when(slideService.updateSlide(id, multipartImg)).thenReturn(dtoResponse);
        mockMvc.perform(multipart("/slides/" + id)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dtoResponse), true))
                .andDo(MockMvcResultHandlers.print());
        verify(slideService).existById(id);
        verify(slideService).updateSlide(id, multipartImg);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateSlide400() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        MockMultipartFile multipartImg = new MockMultipartFile(
                "file",
                "",
                "image/jpeg", "".getBytes());

        when(slideService.existById(id)).thenReturn(true);
        when(slideService.updateSlide(id, multipartImg)).thenReturn(dtoResponse);
        mockMvc.perform(multipart("/slides/" + id)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(content().json(objectMapper.writeValueAsString(dtoResponse), true))
                .andDo(MockMvcResultHandlers.print());
        verify(slideService).existById(id);
        verify(slideService, never()).updateSlide(id, multipartImg);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateSlide404() throws Exception {
        SlideResponseDto dtoResponse = ObjectMapperUtils.map(slide, SlideResponseDto.class);
        MockMultipartFile multipartImg = new MockMultipartFile(
                "file",
                "imagen.jpg",
                "image/jpeg", "imagen.jpg".getBytes());

        when(slideService.existById("test")).thenReturn(false);
        when(slideService.updateSlide("test", multipartImg)).thenReturn(dtoResponse);
        mockMvc.perform(multipart("/slides/" + id)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        verify(slideService).existById(id);
        verify(slideService, never()).updateSlide(id, multipartImg);
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

        when(slideService.deleteSlide(id)).thenReturn(false);
        mockMvc.perform(delete("/testimonials/test"))
                .andExpect(status().isNotFound());
        verify(slideService).deleteSlide("test");
    }
}