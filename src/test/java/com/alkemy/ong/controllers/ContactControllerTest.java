package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.contact.EntryContactDto;
import com.alkemy.ong.dto.response.contact.BasicContactDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.ContactEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.ContactService;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = ContactController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
				classes = {WebSecurityConfiguration.class, JwtTokenFilter.class})},
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContactService contactService;

	String id;
	ContactEntity contact;
	ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		id = "546464654";
		contact = new ContactEntity(id, "Pedro", "3145556678", "pedro@mail.com", "new contact",
				LocalDateTime.now(), false);
		objectMapper = new ObjectMapper();
	}

	@Test
	void createContactWithValidDtoReturnedBasicContactDtoStatus201() throws Exception {
		EntryContactDto entryContactDto = ObjectMapperUtils.map(contact, EntryContactDto.class);
		BasicContactDto contactDto = ObjectMapperUtils.map(contact, BasicContactDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("contacts", "contacts.json",
				"application/json", objectMapper.writeValueAsString(entryContactDto).getBytes());

		when(contactService.save(any())).thenReturn(contact);

		mockMvc.perform(multipart("/contacts").file(multipartEntryDto))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(contactDto), true));

		verify(contactService).save(any());
	}

	@Test
	void createContactWithInvalidDtoThrowValidationExceptionStatus400() throws Exception {
		contact.setName(null);
		EntryContactDto entryContactDto = ObjectMapperUtils.map(contact, EntryContactDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("contacts", "contacts.json", "application/json",
				objectMapper.writeValueAsString(entryContactDto).getBytes());

		mockMvc.perform(multipart("/contacts").file(multipartEntryDto))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
				.andExpect(result -> assertEquals("There are validation errors.",
						result.getResolvedException().getMessage()));

		verify(contactService, never()).save(any());
	}

	@Test
	void getAllWithExistingContactsReturnedListBasicContactDtoStatus200() throws Exception {
		BasicContactDto basicContactDto = ObjectMapperUtils.map(contact, BasicContactDto.class);

		when(contactService.getAllContacts()).thenReturn(Arrays.asList(basicContactDto));

		mockMvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(basicContactDto)), true));

		verify(contactService).getAllContacts();
	}

	@Test
	void getAllNoExistingContactsReturnedStatus404() throws Exception {

		when(contactService.getAllContacts()).thenThrow(ResponseStatusException.class);

		mockMvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(contactService).getAllContacts();
	}

}
