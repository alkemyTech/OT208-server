package com.alkemy.ong.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.exeptions.ArgumentRequiredException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.security.enums.RolName;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = UserController.class, excludeFilters = {
    	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
    	classes = { WebSecurityConfiguration.class, JwtTokenFilter.class})},  
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	String id;
	RoleEntity role;
	UserEntity user;
	ObjectMapper objectMapper;
	
	@BeforeEach
	void setup() {
		id = "546464654";
		role = new RoleEntity("213123213", "Administrador", LocalDateTime.now(), RolName.ROLE_ADMIN);
		user = new UserEntity(id, "Carlos", "Suarez", "cz@gmail.com", "12345678", null, Arrays.asList(role), LocalDateTime.now(), false);
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void deleteUser_existsById_returnedStatus200() throws Exception {
		
		when(userService.deleteUser(id)).thenReturn(true);
		
		mockMvc.perform(delete("/users/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE.concat(";charset=UTF-8")))
				.andExpect( content().string("Usuario eliminado"));
		
		verify(userService).deleteUser(id);	
	}
	
	@Test
	void deleteUser_doesNotExistById_returnedStatus404() throws Exception {
		when(userService.deleteUser(id)).thenReturn(false);
		
		mockMvc.perform(delete("/users/" + id))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE.concat(";charset=UTF-8")))
				.andExpect( content().string("Usuario no eliminado"));


		verify(userService).deleteUser(id);
	}
	
	@Test
	void deleteUser_nullId_returnedStatus400() throws Exception {
		when(userService.deleteUser(id)).thenThrow( new NullPointerException());
		
		mockMvc.perform(delete("/users/" + id))
				.andExpect(status().isBadRequest());
		
		verify(userService).deleteUser(id);	
	}
	
	@Test
	void update_existsById_returnedBasicUserDtoStatus200() throws Exception {
		BasicUserDto userDto = ObjectMapperUtils.map(user, BasicUserDto.class);
		UserRegisterDto userRegisterDto = ObjectMapperUtils.map(user, UserRegisterDto.class);
		String jsonUserRegister = objectMapper.writeValueAsString(userRegisterDto);
		
		when(userService.updateUser(userRegisterDto, id)).thenReturn(userDto);
		
		mockMvc.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonUserRegister))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(userDto), true));
		
		verify(userService).updateUser(userRegisterDto, id);
	}
	
	@Test
	void update_doesNotExistById_returnedStatus404() throws Exception {
		UserRegisterDto userRegisterDto = ObjectMapperUtils.map(user, UserRegisterDto.class);
		String jsonUserRegister = objectMapper.writeValueAsString(userRegisterDto);
		id = "1111111";
		
		when(userService.updateUser(userRegisterDto, id)).thenThrow(ArgumentRequiredException.class);
		
		mockMvc.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonUserRegister))
				.andExpect(status().isNotFound());
		
		verify(userService).updateUser(userRegisterDto, id);
	}
	
	@Test
	void getAll_withExistingUsers_returnedListUserRegisterDtoStatus200() throws JsonProcessingException, Exception {
		UserRegisterDto userDto = ObjectMapperUtils.map(user, UserRegisterDto.class);
		
		when(userService.getAll()).thenReturn(Arrays.asList(userDto));
		
		mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(userDto)), true));
		
		verify(userService).getAll();
	}
	
	@Test
	void getAll_noExistingUsers_returnedStatus404() throws JsonProcessingException, Exception {
		
		when(userService.getAll()).thenThrow(ResponseStatusException.class);
		
		mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
		verify(userService).getAll();
	}
		
}
