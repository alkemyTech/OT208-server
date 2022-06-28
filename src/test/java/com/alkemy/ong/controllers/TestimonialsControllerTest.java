package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.testimonial.EntryTestimonialDto;
import com.alkemy.ong.dto.response.testimonial.BasicTestimonialDTo;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.TestimonialsEntity;
import com.alkemy.ong.services.TestimonialsService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
class TestimonialsControllerTest {

    ObjectMapper objectMapper;
    String id;
    TestimonialsEntity entity;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestimonialsService service;

    @BeforeEach
    void setUp() {
        id = "5e8f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f";
        entity = new TestimonialsEntity(id,
                "Alkemy Name",
                null,
                null, LocalDateTime.now(),
                false);

        objectMapper = new ObjectMapper();
    }

    // POST TESTIMONIALS
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testCreateTestimonialWhithImg200() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("Test Name", "Test Description");
        TestimonialsEntity testEntity = ObjectMapperUtils.map(entryDto, TestimonialsEntity.class);
        BasicTestimonialDTo testDto = ObjectMapperUtils.map(testEntity, BasicTestimonialDTo.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        Mockito.when(service.createTestimonial(entryDto, multipartImg)).thenReturn(testDto);
        mockMvc.perform(
                        multipart("/testimonials")
                                .file(multipartJson)
                                .file(multipartImg))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(testDto)));

        verify(service).createTestimonial(entryDto, multipartImg);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testCreateTestimonialWhithOutImg200() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("Test Name", "Test Description");
        TestimonialsEntity testEntity = ObjectMapperUtils.map(entryDto, TestimonialsEntity.class);
        BasicTestimonialDTo testDto = ObjectMapperUtils.map(testEntity, BasicTestimonialDTo.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "image/jpeg", "".getBytes());

        Mockito.when(service.createTestimonial(entryDto)).thenReturn(testDto);
        mockMvc.perform(
                        multipart("/testimonials")
                                .file(multipartJson)
                                .file(multipartImg))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(testDto)));

        verify(service).createTestimonial(entryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testCreateTestimonialValidation400() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("", "Test Description");
        TestimonialsEntity testEntity = ObjectMapperUtils.map(entryDto, TestimonialsEntity.class);
        BasicTestimonialDTo testDto = ObjectMapperUtils.map(testEntity, BasicTestimonialDTo.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "image/jpeg", "".getBytes());

        mockMvc.perform(
                        multipart("/testimonials")
                                .file(multipartJson)
                                .file(multipartImg))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());

        verify(service, never()).createTestimonial(entryDto);
    }

    // PUT TESTIMONIALS
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testEditTestimonialValidation400() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("", "Test Description");
        TestimonialsEntity testEntity = ObjectMapperUtils.map(entryDto, TestimonialsEntity.class);
        BasicTestimonialDTo testDto = ObjectMapperUtils.map(testEntity, BasicTestimonialDTo.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "image/jpeg", "".getBytes());

        mockMvc.perform(multipart("/testimonials/" + id)
                        .file(multipartJson)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verify(service, never()).updateTestimonial(id, entryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testEditTestimonialNotFound404() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("Test Name", "Test Description");
        TestimonialsEntity testEntity = ObjectMapperUtils.map(entryDto, TestimonialsEntity.class);
        
        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "image/jpeg", "".getBytes());
        when(service.existById(id)).thenReturn(false);
        mockMvc.perform(multipart("/testimonials/anyId")
                        .file(multipartJson)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        verify(service).existById("anyId");
        verify(service, never()).updateTestimonial(id, entryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testEditTestimonialWhithImg200() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("Test Name", "Test Description");
        BasicTestimonialDTo testDto = ObjectMapperUtils.map(entity, BasicTestimonialDTo.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        when(service.existById(id)).thenReturn(true);
        when(service.updateTestimonial(id, entryDto, multipartImg)).thenReturn(testDto);
        mockMvc.perform(multipart("/testimonials/" + id)
                        .file(multipartJson)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(testDto), true))
                .andDo(MockMvcResultHandlers.print());
        verify(service).existById(id);
        verify(service).updateTestimonial(id, entryDto, multipartImg);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testEditTestimonialWithoutImg200() throws Exception {
        EntryTestimonialDto entryDto = new EntryTestimonialDto("Test Name", "Test Description");
        BasicTestimonialDTo testDto = ObjectMapperUtils.map(entity, BasicTestimonialDTo.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "file",
                "test.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "image/jpeg", "".getBytes());

        when(service.existById(id)).thenReturn(true);
        when(service.updateTestimonial(id, entryDto)).thenReturn(testDto);
        mockMvc.perform(multipart("/testimonials/" + id)
                        .file(multipartJson)
                        .file(multipartImg)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(testDto), true))
                .andDo(MockMvcResultHandlers.print());
        verify(service).existById(id);
        verify(service).updateTestimonial(id, entryDto);
    }

    // GET TESTIMONIALS
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testGetTestimonialPage200() throws Exception {
        Page<BasicTestimonialDTo> dToPage = Mockito.mock(Page.class);
        when(service.getTestimonials(any(Pageable.class))).thenReturn(dToPage);

        mockMvc.perform(get("/testimonials/list"))
                .andExpect(status().isOk());
        verify(service).getTestimonials(any(Pageable.class));
    }

    // DELETE TESTIMONIALS
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testDeleteTestimonial204() throws Exception {
        when(service.deleteTestimonial(id)).thenReturn(true);
        mockMvc.perform(delete("/testimonials/" + id))
                .andExpect(status().isNoContent());
        verify(service).deleteTestimonial(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void testDeleteTestimonial404() throws Exception {
        when(service.deleteTestimonial(id)).thenReturn(false);
        mockMvc.perform(delete("/testimonials/test"))
                .andExpect(status().isNotFound());
        verify(service).deleteTestimonial("test");
    }

}
