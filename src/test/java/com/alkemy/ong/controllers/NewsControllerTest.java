package com.alkemy.ong.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.alkemy.ong.dto.request.news.EntryEditNewsDto;
import com.alkemy.ong.dto.request.news.EntryNewsDto;
import com.alkemy.ong.dto.response.news.BasicNewsDto;
import com.alkemy.ong.exeptions.CategoryNotExistException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@WebMvcTest(value = NewsController.class, excludeFilters = {
    	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
    	classes = { WebSecurityConfiguration.class, JwtTokenFilter.class})},  
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class NewsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private NewsService newsService;
	
	@MockBean
	private AWSS3Service awss3Service;
	
	@MockBean
	private CategoryService categoryService;
	
	private String id;
	private String idCategory;
	private CategoryEntity categoryEntity;
	private NewsEntity newsEntity;
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setup() {
		id = "123456";
		idCategory = "1237485";
		categoryEntity = new CategoryEntity(idCategory, "Romance", "categoria de descripcion", null, 
				LocalDateTime.now(), false);
		newsEntity = new NewsEntity(id, "Se encontraron", "Algo de texto", "miImagen.jpg", categoryEntity,
				"news", false, LocalDateTime.now());
		
		objectMapper = new ObjectMapper();
		DateFormat dateFormat = objectMapper.getDateFormat();
		objectMapper.registerModule( new JavaTimeModule());
		objectMapper.setDateFormat(dateFormat);
	}
	
	@Test
	void getNews_existsById_returnedBasicNewsDtoStatus200() throws Exception {
		BasicNewsDto newsDto = ObjectMapperUtils.map(newsEntity, BasicNewsDto.class);
		
		when(newsService.findById(id)).thenReturn(Optional.of(newsEntity));
		
		mockMvc.perform(get("/news/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(newsDto), true));
		
		verify(newsService).findById(id);
	}
	
	@Test
	void getNews_doesNotExistById_returnedStatus404() throws Exception {
		when(newsService.findById(id)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/news/" + id))
				.andExpect(status().isNotFound());
		
		verify(newsService).findById(id);
	}
	
	@Test
	void createNews_withValidDtoAndImage_returnedBasicNewsDtoStatus201() throws Exception {
		EntryNewsDto entryNewsDto = ObjectMapperUtils.map(newsEntity, EntryNewsDto.class);
		BasicNewsDto newsDto = ObjectMapperUtils.map(newsEntity, BasicNewsDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		when(categoryService.existById(idCategory)).thenReturn(true);
		when(awss3Service.uploadFile(multipartImage)).thenReturn("miImagen.jpg");
		when(newsService.save(any())).thenReturn(newsEntity);
		
		mockMvc.perform(multipart("/news").file(multipartEntryDto).file(multipartImage))
					.andExpect(status().isCreated())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json(objectMapper.writeValueAsString(newsDto), true));
		
		verify(categoryService).existById(idCategory);
		verify(awss3Service).uploadFile(multipartImage);
		verify(newsService).save(any());
	}
	
	@Test
	void createNews_withInvalidDtoAndImage_throwValidationExceptionStatus400() throws Exception {
		newsEntity.setName(null);
		EntryNewsDto entryNewsDto = ObjectMapperUtils.map(newsEntity, EntryNewsDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		mockMvc.perform(multipart("/news").file(multipartEntryDto).file(multipartImage))
					.andExpect(status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
				    .andExpect(result -> assertEquals("There are validation errors.", 
				    		result.getResolvedException().getMessage()));
		
		
		verify(categoryService, never()).existById(any());
		verify(awss3Service, never()).uploadFile(any());
		verify(newsService, never()).save(any());
	}
	
	@Test
	void createNews_noExistentCategory_throwCategoryNotExistExceptionStatus404() throws Exception {
		EntryNewsDto entryNewsDto = ObjectMapperUtils.map(newsEntity, EntryNewsDto.class);
		MockMultipartFile multipartEntryDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		when(categoryService.existById(idCategory)).thenReturn(false);
		
		mockMvc.perform(multipart("/news").file(multipartEntryDto).file(multipartImage))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryNotExistException))
				    .andExpect(result -> assertEquals("The category with the id: " + idCategory + " does not exist",
				    		result.getResolvedException().getMessage()));
		
		
		verify(categoryService).existById(idCategory);
		verify(awss3Service, never()).uploadFile(any());
		verify(newsService, never()).save(any());
	}
	
	@Test
	void editNews_withValidDtoAndImage_returnedBasicNewsDtoStatus200() throws Exception {
		EntryEditNewsDto entryEditNewsDto = ObjectMapperUtils.map(newsEntity, EntryEditNewsDto.class);
		entryEditNewsDto.setCategory(idCategory);
		BasicNewsDto newsDto = ObjectMapperUtils.map(newsEntity, BasicNewsDto.class);
		MockMultipartFile multipartEntryEditDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryEditNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		when(categoryService.existById(idCategory)).thenReturn(true);
		when(newsService.findById(id)).thenReturn(Optional.of(newsEntity));
		when(categoryService.findById(idCategory)).thenReturn(Optional.of(categoryEntity));
		when(awss3Service.uploadFile(multipartImage)).thenReturn("miImagen.jpg");
		when(newsService.edit(any())).thenReturn(newsEntity);
		
		mockMvc.perform(multipart("/news/" + id).file(multipartEntryEditDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json(objectMapper.writeValueAsString(newsDto), true));
		
		verify(categoryService).existById(idCategory);
		verify(newsService).findById(id);
		verify(categoryService).findById(idCategory);
		verify(awss3Service).uploadFile(multipartImage);
		verify(newsService).edit(any());
	}
	
	@Test
	void editNews_withinvalidDtoAndImage_throwValidationExceptionDtoStatus400() throws Exception {
		EntryEditNewsDto entryEditNewsDto = ObjectMapperUtils.map(newsEntity, EntryEditNewsDto.class);
		entryEditNewsDto.setCategory(null);
		MockMultipartFile multipartEntryEditDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryEditNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		mockMvc.perform(multipart("/news/" + id).file(multipartEntryEditDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isBadRequest());
		
		verify(categoryService, never()).existById(idCategory);
		verify(newsService, never()).findById(id);
		verify(categoryService, never()).findById(idCategory);
		verify(awss3Service, never()).uploadFile(multipartImage);
		verify(newsService, never()).edit(any());
	}
	
	@Test
	void editNews_noExistentCategory_throwCategoryNotExistExceptionStatus404() throws Exception {
		EntryEditNewsDto entryEditNewsDto = ObjectMapperUtils.map(newsEntity, EntryEditNewsDto.class);
		entryEditNewsDto.setCategory(idCategory);
		MockMultipartFile multipartEntryEditDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryEditNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		when(categoryService.existById(idCategory)).thenReturn(false);
		
		mockMvc.perform(multipart("/news/" + id).file(multipartEntryEditDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryNotExistException))
				    .andExpect(result -> assertEquals("The category with the id: " + idCategory + " does not exist",
				    		result.getResolvedException().getMessage()));
		
		
		verify(categoryService).existById(idCategory);
		verify(newsService, never()).findById(id);
		verify(categoryService, never()).findById(idCategory);
		verify(awss3Service, never()).uploadFile(multipartImage);
		verify(newsService, never()).edit(any());
	}
	
	@Test
	void editNews_doesNotExistById_returnedStatus404() throws Exception {
		EntryEditNewsDto entryEditNewsDto = ObjectMapperUtils.map(newsEntity, EntryEditNewsDto.class);
		entryEditNewsDto.setCategory(idCategory);
		MockMultipartFile multipartEntryEditDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryEditNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", "miImagen.jpg", "image/jpeg", 
				"miImagen.jpg".getBytes());
		
		when(categoryService.existById(idCategory)).thenReturn(true);
		when(newsService.findById(id)).thenReturn(Optional.empty());
		
		mockMvc.perform(multipart("/news/" + id).file(multipartEntryEditDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isNotFound());
		
		
		verify(categoryService).existById(idCategory);
		verify(newsService).findById(id);
		verify(categoryService, never()).findById(idCategory);
		verify(awss3Service, never()).uploadFile(multipartImage);
		verify(newsService, never()).edit(any());
	}
	
	@Test
	void editNews_withValidDtoAndNullImage_returnedBasicNewsDtoStatus200() throws Exception {
		EntryEditNewsDto entryEditNewsDto = ObjectMapperUtils.map(newsEntity, EntryEditNewsDto.class);
		entryEditNewsDto.setCategory(idCategory);
		BasicNewsDto newsDto = ObjectMapperUtils.map(newsEntity, BasicNewsDto.class);
		MockMultipartFile multipartEntryEditDto = new MockMultipartFile("news", "news.json", "application/json",
				objectMapper.writeValueAsString(entryEditNewsDto).getBytes());
		MockMultipartFile multipartImage =  new MockMultipartFile("newsImage", null, "image/jpeg", 
				"".getBytes());
		
		when(categoryService.existById(idCategory)).thenReturn(true);
		when(newsService.findById(id)).thenReturn(Optional.of(newsEntity));
		when(categoryService.findById(idCategory)).thenReturn(Optional.of(categoryEntity));
		when(awss3Service.uploadFile(multipartImage)).thenReturn("miImagen.jpg");
		when(newsService.edit(any())).thenReturn(newsEntity);
		
		mockMvc.perform(multipart("/news/" + id).file(multipartEntryEditDto).file(multipartImage)
				.with( request -> {request.setMethod("PUT"); return request;}))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json(objectMapper.writeValueAsString(newsDto), true));
		
		verify(categoryService).existById(idCategory);
		verify(newsService).findById(id);
		verify(categoryService).findById(idCategory);
		verify(awss3Service, never()).uploadFile(any());
		verify(newsService).edit(any());
	}
	
	@Test
	void deleteNews_existsById_returnedStatus204() throws Exception {		
		when(newsService.findById(id)).thenReturn(Optional.of(newsEntity));
		doNothing().when(newsService).delete(newsEntity);
		
		mockMvc.perform(delete("/news/" + id))
				.andExpect(status().isNoContent());
		
		verify(newsService).findById(id);
		verify(newsService).delete(newsEntity);
	}
	
	@Test
	void deleteNews_doesNotExistById_returnedStatus404() throws Exception {		
		when(newsService.findById(id)).thenReturn(Optional.empty());
		
		mockMvc.perform(delete("/news/" + id))
				.andExpect(status().isNotFound());
		
		verify(newsService).findById(id);
		verify(newsService, never()).delete(newsEntity);
	}
	
	@Test
	void getNews_whenExistNews_returnedPageOfBasicNewsDtoStatus200() throws Exception {
		BasicNewsDto newsDto = ObjectMapperUtils.map(newsEntity, BasicNewsDto.class);
		Page<BasicNewsDto> pageOfNewsDto = new PageImpl<BasicNewsDto>(Arrays.asList(newsDto));
		
		when(newsService.getNews(any())).thenReturn(pageOfNewsDto);
		
		mockMvc.perform(get("/news/list"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(
						objectMapper.writeValueAsString(pageOfNewsDto), true));
		
		verify(newsService).getNews(any());
	}
	
	@Test
	void getNews_noExistNews_throwResponseStatusExceptionStatus404() throws Exception {
		when(newsService.getNews(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		mockMvc.perform(get("/news/list"))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
		
		verify(newsService).getNews(any());
	}
	
}
