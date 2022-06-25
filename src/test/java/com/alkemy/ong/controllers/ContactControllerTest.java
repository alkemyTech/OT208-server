package com.alkemy.ong.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.alkemy.ong.dto.request.contact.EntryContactDto;
import com.alkemy.ong.dto.response.contact.BasicContactDto;
import com.alkemy.ong.exeptions.EmailNotSendException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.ContactEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.ContactService;
import com.alkemy.ong.services.EmailService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(value = ContactController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
				classes = {WebSecurityConfiguration.class, JwtTokenFilter.class})},
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContactService contactService;
	
	@MockBean
	private EmailService emailService;

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
	void create_withValidDto_returnedBasicContactDtoStatus201() throws Exception {
		EntryContactDto entryContactDto = ObjectMapperUtils.map(contact, EntryContactDto.class);
		BasicContactDto contactDto = ObjectMapperUtils.map(contact, BasicContactDto.class);

		when(contactService.saveContact(entryContactDto)).thenReturn(contactDto);
		when(emailService.sendEmailContactForm(entryContactDto.getEmail())).thenReturn(any());

		mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(entryContactDto)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(contactDto), true));

		verify(contactService).saveContact(entryContactDto);
		verify(emailService).sendEmailContactForm(entryContactDto.getEmail());
	}

	@Test
	void create_withInvalidEmail_throwEmailNotSendExceptionStatus400() throws Exception {
		EntryContactDto entryContactDto = ObjectMapperUtils.map(contact, EntryContactDto.class);
		BasicContactDto contactDto = ObjectMapperUtils.map(contact, BasicContactDto.class);
		
		when(contactService.saveContact(entryContactDto)).thenReturn(contactDto);
		when(emailService.sendEmailContactForm(entryContactDto.getEmail())).thenThrow(new EmailNotSendException("Error registering email."));

		mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(entryContactDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof EmailNotSendException))
				.andExpect(result -> assertEquals("Error registering email.",
						result.getResolvedException().getMessage()));

		verify(contactService, never()).saveContact(entryContactDto);
		verify(emailService).sendEmailContactForm(entryContactDto.getEmail());
	}
	
	@Test
	void createContactWithInvalidDtoThrowValidationExceptionStatus400() throws Exception {
		contact.setName(null);
		EntryContactDto entryContactDto = ObjectMapperUtils.map(contact, EntryContactDto.class);

		mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(entryContactDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
				.andExpect(result -> assertEquals("There are validation errors.",
						result.getResolvedException().getMessage()));

		verify(contactService, never()).saveContact(entryContactDto);
	}

	@Test
	void getAll_withExistingContacts_returnedListBasicContactDtoStatus200() throws Exception {
		BasicContactDto basicContactDto = ObjectMapperUtils.map(contact, BasicContactDto.class);

		when(contactService.getAllContacts()).thenReturn(Arrays.asList(basicContactDto));

		mockMvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(basicContactDto)), true));

		verify(contactService).getAllContacts();
	}

	@Test
	void getAll_noExistingContacts_returnedStatus404() throws Exception {

		when(contactService.getAllContacts()).thenReturn(Arrays.asList());

		mockMvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(contactService).getAllContacts();
	}

}
