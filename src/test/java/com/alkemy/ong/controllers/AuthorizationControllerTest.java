package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.security.enums.RolName;
import com.alkemy.ong.services.EmailService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthorizationController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = { WebSecurityConfiguration.class, JwtTokenFilter.class})},
        excludeAutoConfiguration = { SecurityAutoConfiguration.class })
public class AuthorizationControllerTest {

    ObjectMapper objectMapper; // mapea a json

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private JwtUtils jwtUtils;

    String id;
    UserEntity userEntity;
    RoleEntity roleEntity;

    @BeforeEach
    void setup() {
        id = "12345678";
        objectMapper = new ObjectMapper();
        roleEntity = new RoleEntity("id", "description", LocalDateTime.now(), RolName.ROLE_USER);
        userEntity = new UserEntity("id", "Martin", "Perez", "email@gmail.com", "password", "photo", List.of(roleEntity), LocalDateTime.now(), false);
    }
/*
    //ANDA
    @Test
    void logIn_Status200() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("email@gmail.com", "password");
        String userLoginJson = objectMapper.writeValueAsString(userLoginDto);

        when(userService.logIn(userLoginDto)).thenReturn("token");

        mockMvc.perform(post("/auth/logIn").contentType(MediaType.APPLICATION_JSON).content(userLoginJson))
				.andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN.toString().concat(";charset=UTF-8")))
                .andExpect(content().string("token"))
                .andDo(MockMvcResultHandlers.print());

        verify(userService).logIn(userLoginDto);
    }

    // ANDA
    @Test
    void logIn_Status404() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("test@gmail.com", "12345678");
        String userLoginJson = objectMapper.writeValueAsString(userLoginDto);

        when(userService.logIn(userLoginDto))
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        mockMvc.perform(post("/auth/logIn").contentType(MediaType.APPLICATION_JSON).content(userLoginJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(userService).logIn(userLoginDto);
    }

    //ANDA
    @Test
    void logIn_BadCredentials() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("email@gmail.com", "badPassword");
        String userLoginJson = objectMapper.writeValueAsString(userLoginDto);

        when(userService.logIn(userLoginDto))
                .thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/auth/logIn").contentType(MediaType.APPLICATION_JSON).content(userLoginJson))
                .andExpect(status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());

        verify(userService).logIn(userLoginDto);
    }

    // ANDA
    @Test
    void logIn_ValidationException() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("", "badPassword");
        String userLoginJson = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/auth/logIn").contentType(MediaType.APPLICATION_JSON).content(userLoginJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()));

        verify(userService, never()).logIn(userLoginDto);
    }

    // ANDA
    @Test
    void singUp_Status200() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto(id, "Juan", "Perez", "correo@gmail.com", "password", "photo");
        String userRegisterJson = objectMapper.writeValueAsString(userRegisterDto);

        when(userService.singUp(userRegisterDto)).thenReturn("token");

        mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON).content(userRegisterJson))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN.toString().concat(";charset=UTF-8")))
                .andExpect(content().string("token"))
                .andDo(MockMvcResultHandlers.print());

        verify(userService).singUp(userRegisterDto);
    }
/*
    @Test
    void singUp_ValidationException() throws Exception {
        UserLoginDto userRegisterDto = new UserLoginDto("", "badPassword");
        String userRegisterJson = objectMapper.writeValueAsString(userRegisterDto);

        mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON).content(userRegisterJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("There are validation errors.",
                        result.getResolvedException().getMessage()));

        verify(userService, never()).singUp(userRegisterDto);
    }
*/
/*
    @Test
    void me_Status200() throws Exception {
        BasicUserDto basicUserDto = ObjectMapperUtils.map(userEntity, BasicUserDto.class);
        String basicUserJson = objectMapper.writeValueAsString(basicUserDto);

        when(jwtUtils.getToken(any())).thenReturn("tokenEmulado");
        when(jwtUtils.extractId("tokenEmulado")).thenReturn("idUser");
        when(userService.findById("idUser")).thenReturn(Optional.of(userEntity));

        mockMvc.perform(get("/auth/me").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "tokenEmulado"))
                .andExpect(status().isOk())
                .andExpect(content().json(basicUserJson, true));
                //.andDo(MockMvcResultHandlers.print());

        verify(userService).findById("idUser");
        verify(jwtUtils).getToken(any());
        verify(jwtUtils).extractId("tokenEmulado");
    }
*/
/* EJEMPLO ADRI
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
*/
}
