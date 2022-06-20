package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.exeptions.ArgumentRequiredException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.ActivityService;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
		activity = new ActivityEntity(id, "Nombre de la actividad", "Contenido", "imagen", LocalDateTime.now(), false);
		objectMapper = new ObjectMapper();
	}

	@Test
	void updateExistsByIdReturnedDtoStatus200() throws Exception {
		BasicActivityDto activityDto = ObjectMapperUtils.map(activity, BasicActivityDto.class);
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		String jsonUserRegister = objectMapper.writeValueAsString(entryActivityDto);

		when(activityService.updateActivity(entryActivityDto, "imagen", id)).thenReturn(activityDto);

		mockMvc.perform(put("/activities/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonUserRegister))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(activityDto), true));

		verify(activityService).updateActivity(entryActivityDto, "imagen", id);
	}

	@Test
	void updateDoesNotExistByIdReturnedStatus404() throws Exception {
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		String jsonUserRegister = objectMapper.writeValueAsString(entryActivityDto);
		id = "1111111";

		when(activityService.updateActivity(entryActivityDto, "", id)).thenThrow(ArgumentRequiredException.class);

		mockMvc.perform(put("/activities/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonUserRegister))
				.andExpect(status().isNotFound());

		verify(activityService).updateActivity(entryActivityDto, "", id);
	}

	@Test
	void createActivityWithValidDtoAndImageReturnedBasicActivityDtoStatus201() throws Exception {
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		BasicActivityDto activityDto = ObjectMapperUtils.map(activity, BasicActivityDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "miImagen.jpg", "image/jpeg",
				"miImagen.jpg".getBytes());

		when(awss3Service.uploadFile(multipartImage)).thenReturn("miImagen.jpg");
		when(activityService.save(any())).thenReturn(activity);

		mockMvc.perform(multipart("/activities").file(multipartEntryDto).file(multipartImage))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(activityDto), true));

		verify(awss3Service).uploadFile(multipartImage);
		verify(activityService).save(any());
	}

	@Test
	void createActivityWithInvalidDtoAndImageThrowValidationExceptionStatus400() throws Exception {
		activity.setName(null);
		EntryActivityDto entryActivityDto = ObjectMapperUtils.map(activity, EntryActivityDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("activity", "activity.json", "application/json",
				objectMapper.writeValueAsString(entryActivityDto).getBytes());
		MockMultipartFile multipartImage = new MockMultipartFile("file", "miImagen.jpg", "image/jpeg",
				"miImagen.jpg".getBytes());

		mockMvc.perform(multipart("/activities").file(multipartEntryDto).file(multipartImage))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
				.andExpect(result -> assertEquals("There are validation errors.",
						result.getResolvedException().getMessage()));

		verify(awss3Service, never()).uploadFile(any());
		verify(activityService, never()).save(any());
	}

}
