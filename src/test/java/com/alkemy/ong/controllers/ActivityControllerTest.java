package com.alkemy.ong.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.time.LocalDateTime;

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
import org.springframework.web.server.ResponseStatusException;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.ActivityService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@WebMvcTest(value = ActivityController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
				classes = {WebSecurityConfiguration.class, JwtTokenFilter.class})},
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ActivityControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ActivityService activityService;

	@MockBean
	private AWSS3Service awss3Service;

	String id;
	ActivityEntity activity;
	ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		id = "546464654";
		activity = new ActivityEntity(id, "Nombre de la actividad", "Contenido", "imagen.jpg", LocalDateTime.now(), false);
		objectMapper = new ObjectMapper();
		DateFormat dateFormat = objectMapper.getDateFormat();
		objectMapper.registerModule( new JavaTimeModule());
		objectMapper.setDateFormat(dateFormat);
	}

	@Test
	void create_withValidDtoAndImage_ReturnedBasicDtoStatus201() throws Exception {
		BasicActivityDto activityDto = ObjectMapperUtils.map(activity, BasicActivityDto.class);
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "news.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("file", "imagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());

		when(activityService.saveActivity(entryActivityDto, "imagen")).thenReturn(activityDto);
		when(awss3Service.uploadFile(multipartImage)).thenReturn("imagen");

		mockMvc.perform(multipart("/activities").file(multipartEntryDto).file(multipartImage).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(activityDto), true));

		verify(activityService).saveActivity(entryActivityDto, "imagen");
		verify(awss3Service).uploadFile(multipartImage);
	}
	
	@Test
	void create_withInvalidDtoAndImage_throwValidationExceptionStatus400() throws Exception {
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		entryActivityDto.setName(null);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());

		mockMvc.perform(multipart("/activities").file(multipartEntryDto).file(multipartImage))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
				.andExpect(result -> assertEquals("There are validation errors.",
						result.getResolvedException().getMessage()));

		verify(awss3Service, never()).uploadFile(any());
		verify(activityService, never()).save(any());
	}
	
	@Test
	void create_withDtoAndNullImage_throwResponseStatusExceptionStatus400() throws Exception {
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "", "image/jpeg",
				"".getBytes());

		mockMvc.perform(multipart("/activities").file(multipartEntryDto).file(multipartImage))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

		verify(awss3Service, never()).uploadFile(any());
		verify(activityService, never()).save(any());
	}
	
	

	@Test
	void update_withValidDtoAndImage_returnedBasicDtoStatus200() throws Exception {
		BasicActivityDto activityDto = ObjectMapperUtils.map(activity, BasicActivityDto.class);
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		when(activityService.updateActivity(entryActivityDto, multipartImage, id)).thenReturn(activityDto);

		mockMvc.perform(multipart("/activities/" + id).file(multipartEntryDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json(objectMapper.writeValueAsString(activityDto), true));

		verify(activityService).updateActivity(entryActivityDto, multipartImage, id);
	}
	
	@Test
	void update_withDtoAndEmptyImage_returnedBasicDtoStatus200() throws Exception {
		BasicActivityDto activityDto = ObjectMapperUtils.map(activity, BasicActivityDto.class);
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "", "image/jpeg", 
				"".getBytes());
		
		when(activityService.updateActivity(entryActivityDto, multipartImage, id)).thenReturn(activityDto);

		mockMvc.perform(multipart("/activities/" + id).file(multipartEntryDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json(objectMapper.writeValueAsString(activityDto), true));

		verify(activityService).updateActivity(entryActivityDto, multipartImage, id);
	}
	
	@Test
	void update_withNoexistentId_returnedBasicDtoStatus200() throws Exception {
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", 
				"imagen.jpg".getBytes());
		id = "cualquiera";
		
		when(activityService.updateActivity(entryActivityDto, multipartImage, id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		mockMvc.perform(multipart("/activities/" + id).file(multipartEntryDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isNotFound());

		verify(activityService).updateActivity(entryActivityDto, multipartImage, id);
	}
	
	@Test
	void update_withInvalidDtoAndImage_throwValidationExceptionStatus400() throws Exception {
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		entryActivityDto.setName(null);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());

		mockMvc.perform(multipart("/activities/" + id).file(multipartEntryDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
					.andExpect(result -> assertEquals("There are validation errors.",
							result.getResolvedException().getMessage()));

		verify(activityService, never()).updateActivity(any(),any(), any());
	}

}
