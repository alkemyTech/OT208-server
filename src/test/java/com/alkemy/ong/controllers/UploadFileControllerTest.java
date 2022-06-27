package com.alkemy.ong.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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

import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.AWSS3Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = UploadFileController.class, excludeFilters = {
    	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
    	classes = { WebSecurityConfiguration.class, JwtTokenFilter.class})},  
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UploadFileControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AWSS3Service awss3Service;
	
	ObjectMapper objectMapper;
	MockMultipartFile multipartImage;
	
	@BeforeEach
	void setup() {
		multipartImage =  new MockMultipartFile("file", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void uploadFile_withValidImage_returnedStringMessageStatus200() throws Exception {
		when(awss3Service.uploadFile(multipartImage)).thenReturn("miImage.jpg");
		
		mockMvc.perform(multipart("/storage/upload").file(multipartImage))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE.concat(";charset=UTF-8")))
				.andExpect(content().string("miImage.jpg File was successfully uploaded"));
	}
	
	@Test
	void uploadFile_withEmptyImage_returnedStatus400() throws Exception {
		multipartImage = new MockMultipartFile("file", "", "image/jpeg", 
				"".getBytes());
		
		mockMvc.perform(multipart("/storage/upload").file(multipartImage))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE.concat(";charset=UTF-8")))
				.andExpect(content().string("Please choose an image file type"));
	}
	
	@Test
	void listFiles_withImages_returnedListOfUrlImagesStatus200() throws Exception {
		List<String> urlImages = Arrays.asList("miImagen", "OtraImagen");
		
		when(awss3Service.getAllObjectsFromS3()).thenReturn(urlImages);
		
		mockMvc.perform(get("/storage/list").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(urlImages), true));
	}
	
	@Test
	void listFiles_noImages_returnedStatus404() throws Exception {
		List<String> urlImages = Arrays.asList();
		
		when(awss3Service.getAllObjectsFromS3()).thenReturn(urlImages);
		
		mockMvc.perform(get("/storage/list").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void download_imageExists_returnedImageStatus200() throws Exception {
		String key = "keyTest";
		when(awss3Service.downloadFile(key)).thenReturn(InputStream.nullInputStream());
		
		mockMvc.perform(get("/storage/download?key=" + key).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	void download_imageNoExists_returnedStatus404() throws Exception {
		String key = "keyTest";
		when(awss3Service.downloadFile(key)).thenThrow(new RuntimeException());
		
		mockMvc.perform(get("/storage/download?key=" + key).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}
