package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.request.organization.EntryOrganizationDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.models.SlideEntity;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
class OrganizationControllerTest {

    String id;
    OrganizationEntity ong;
    SlideEntity slide1;
    SlideEntity slide2;
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService organizationService;

    @BeforeEach
    void setup() {
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
        slide1 = new SlideEntity("1111111",
                "http:/anotherImage1.jpg",
                "one text",
                1, ong);
        slide2 = new SlideEntity("2222222",
                "http:/anotherImage2.jpg",
                "one text",
                2, ong);
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "userMock", authorities = "NON_REGISTER")
    void publicDataResponse200() throws Exception {
        OrganizationPublicDto dto = ObjectMapperUtils.map(ong, OrganizationPublicDto.class);
        when(organizationService.getPublicOrganizationData()).thenReturn(dto);
        mockMvc.perform(get("/organization/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "userMock", authorities = "NON_REGISTER")
    void publicDataResponse200_whenSlidesNull() throws Exception {
        slide1 = null;
        slide2 = null;
        OrganizationPublicDto dto = ObjectMapperUtils.map(ong, OrganizationPublicDto.class);
        when(organizationService.getPublicOrganizationData()).thenReturn(dto);
        mockMvc.perform(get("/organization/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @WithMockUser(username = "userMock", authorities = "NON_REGISTER")
    void publicDataResponse404() throws Exception {
        OrganizationPublicDto dto = ObjectMapperUtils.map(ong, OrganizationPublicDto.class);
        dto.setName(null);
        when(organizationService.getPublicOrganizationData()).thenReturn(dto);
        mockMvc.perform(get("/organization/public"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateOrganizationResponse200() throws Exception {
        EntryOrganizationDto entryDto = new EntryOrganizationDto(
                "OtherName",
                "http://anotherImage.jpg",
                11111122,
                "another@mail.com",
                "another text",
                "Any Adress for ong",
                "https://www.facebook.com/otherName",
                "https://www.instagram.com/otherName",
                "https://www.linkedin.com/otherName");

        OrganizationEntity updatedOng = new OrganizationEntity(id,
                "OtherName",
                "http://anotherImage.jpg",
                "Any Adress for ong",
                11111122,
                "another@mail.com",
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...",
                "another text",
                "https://www.facebook.com/otherName",
                "https://www.instagram.com/otherName",
                "https://www.linkedin.com/otherName",
                ong.getTimestamps(),
                false);

        OrganizationPublicDto dto = ObjectMapperUtils.map(updatedOng, OrganizationPublicDto.class);

        when(organizationService.updateOrganization(entryDto)).thenReturn(dto);
        mockMvc.perform(post("/organization/public").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateOrganizationResponse400() throws Exception {
        EntryOrganizationDto entryDto = new EntryOrganizationDto();
        entryDto.setName("A");
        mockMvc.perform(post("/organization/public").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "userMock", authorities = "NON_REGISTER")
    void updateOrganizationResponse403_NonRegister() throws Exception {
        when(organizationService.updateOrganization(new EntryOrganizationDto()))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));
        mockMvc.perform(post("/organization/public").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EntryOrganizationDto())))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void updateOrganizationResponse403_User() throws Exception {
        when(organizationService.updateOrganization(new EntryOrganizationDto()))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));
        mockMvc.perform(post("/organization/public").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EntryOrganizationDto())))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateOrganizationResponse404() throws Exception {
        ong = null;
        when(organizationService.updateOrganization(new EntryOrganizationDto())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform(post("/organization/public").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EntryOrganizationDto())))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }


}
