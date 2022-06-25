package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.members.EditMemberDto;
import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.services.MemberService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    MemberEntity memberEntity;
    EditMemberDto editDto;
    ObjectMapper objectMapper;
    String id;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setup() {
        id = "5e8f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f";
        memberEntity = new MemberEntity(id,
                "Test Name",
                null,
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                false);
        editDto = new EditMemberDto(
                "Alkemy Name",
                "https://facebook/alkemy",
                "https://instagram/alkemy",
                "https://linkedin/alkemy",
                "a description for test");
        objectMapper = new ObjectMapper();
    }

    //              GET TEST
    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void getMembersAdmin200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members?page=0&size=10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "userMock", authorities = "NON_REGISTER")
    void getMembersNonLogin403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members".concat("?page=")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void getMembersUser403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members".concat("?page=")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    //                  POST TEST

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void createMemberWithMultipart201() throws Exception {

        EntryMemberDto entryDto = new EntryMemberDto("Test Name");
        MemberEntity member = ObjectMapperUtils.map(entryDto, MemberEntity.class);
        MemberResponseDto responseDto = ObjectMapperUtils.map(member, MemberResponseDto.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        Mockito.when(memberService.createMember(entryDto, multipartImg)).thenReturn(responseDto);

        RequestBuilder request = MockMvcRequestBuilders.multipart("/members")
                .file(multipartJson)
                .file(multipartImg);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto), true))
                .andDo(MockMvcResultHandlers.print());

        verify(memberService).createMember(entryDto, multipartImg);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void createMemberWithoutMultipart201() throws Exception {

        EntryMemberDto entryDto = new EntryMemberDto("Test Name");
        MemberEntity member = ObjectMapperUtils.map(entryDto, MemberEntity.class);
        MemberResponseDto responseDto = ObjectMapperUtils.map(member, MemberResponseDto.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "", "".getBytes());

        Mockito.when(memberService.createMember(entryDto)).thenReturn(responseDto);

        RequestBuilder request = MockMvcRequestBuilders.multipart("/members")
                .file(multipartJson)
                .file(multipartImg);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(MockMvcResultHandlers.print());

        verify(memberService).createMember(entryDto);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void createMemberValidation400() throws Exception {

        EntryMemberDto entryDto = new EntryMemberDto("A");//name is too short

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        RequestBuilder request = MockMvcRequestBuilders.multipart("/members")
                .file(multipartJson)
                .file(multipartImg);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "userMock", authorities = "NON_REGISTER")
    void createMemberForbidden403() throws Exception {

        EntryMemberDto entryDto = new EntryMemberDto("John Doe");

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(entryDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        RequestBuilder request = MockMvcRequestBuilders.multipart("/members")
                .file(multipartJson)
                .file(multipartImg);

        mockMvc.perform(request)
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

    }

    //              DELETE TEST

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void deleteMember204() throws Exception {
        when(memberService.existById(id)).thenReturn(true);
        when(memberService.deleteMember(id)).thenReturn("Member was successfully deleted");

        mockMvc.perform(delete("/members/" + id))
                .andExpect(status().isNoContent());
        verify(memberService).existById(id);
        verify(memberService).deleteMember(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void deleteMember404() throws Exception {
        mockMvc.perform(delete("/members/test")) //test is not a valid id
                .andExpect(status().isNotFound());
        verify(memberService).existById("test");
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void deleteMemberForbidden403() throws Exception {
        mockMvc.perform(delete("/members/" + id))
                .andExpect(status().isForbidden());
    }

    //              PUT TEST

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateMemberValidation400() throws Exception {

        editDto.setName("A");//name is too short

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(editDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        mockMvc.perform(multipart("/members/" + id).file(multipartJson).file(multipartImg)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void updateMemberForbidden403() throws Exception {
        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(editDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        mockMvc.perform(multipart("/members/" + id).file(multipartJson).file(multipartImg)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateMemberNotFound404() throws Exception {

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(editDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        mockMvc.perform(multipart("/members/test").file(multipartJson).file(multipartImg)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(memberService).existById("test");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateMemberWhitMultipartOk200() throws Exception {
        MemberEntity member = ObjectMapperUtils.map(editDto, memberEntity);
        MemberResponseDto responseDto = ObjectMapperUtils.map(member, MemberResponseDto.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(editDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "anyImage.jpg",
                "image/jpeg", "anyImage.jpg".getBytes());

        when(memberService.existById(id)).thenReturn(true);
        when(memberService.updateMember(editDto,id,multipartImg)).thenReturn(responseDto);

        mockMvc.perform(multipart("/members/"+ id).file(multipartJson).file(multipartImg)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(memberService).existById(id);
        verify(memberService).updateMember(editDto, id, multipartImg);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void updateMemberWhitOutMultipartOk200() throws Exception {
        MemberEntity member = ObjectMapperUtils.map(editDto, memberEntity);
        MemberResponseDto responseDto = ObjectMapperUtils.map(member, MemberResponseDto.class);

        MockMultipartFile multipartJson = new MockMultipartFile(
                "dto",
                "member.json",
                "application/json", objectMapper.writeValueAsString(editDto).getBytes());
        MockMultipartFile multipartImg = new MockMultipartFile(
                "img",
                "",
                "", "".getBytes());

        when(memberService.existById(id)).thenReturn(true);
        when(memberService.updateMember(editDto,id,multipartImg)).thenReturn(responseDto);

        mockMvc.perform(multipart("/members/"+ id).file(multipartJson).file(multipartImg)
                        .with( request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(memberService).existById(id);
        verify(memberService).updateMember(editDto, id);
    }
}