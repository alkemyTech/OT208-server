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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.alkemy.ong.dto.request.comment.EditCommentDto;
import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.comment.BasicCommentDto;
import com.alkemy.ong.dto.response.comment.CompleteCommentDto;
import com.alkemy.ong.exeptions.NewsNotExistException;
import com.alkemy.ong.exeptions.UserNotExistException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtTokenFilter;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.models.CommentEntity;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.security.WebSecurityConfiguration;
import com.alkemy.ong.security.enums.RolName;
import com.alkemy.ong.services.CommentService;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = CommentController.class, excludeFilters = {
    	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
    	classes = { WebSecurityConfiguration.class, JwtTokenFilter.class})},  
		excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CommentService commentService;
	
	@MockBean
	private NewsService newsService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	private String idComment;
	private String idUser;
	private String idNews;
	private String idCategory;
	private CategoryEntity categoryEntity;
	private NewsEntity newsEntity;
	private ObjectMapper objectMapper;
	private CommentEntity commentEntity;
	private UserEntity userEntity;
	private RoleEntity roleEntity;
	
	@BeforeEach
	void setup() {
		idNews = "123456";
		idCategory = "1237485";
		idComment = "36544841";
		idUser = "123454654";
		categoryEntity = new CategoryEntity(idCategory, "Romance", "categoria de descripcion", null, 
				LocalDateTime.now(), false);
		roleEntity = new RoleEntity("213123213", "Administrador", LocalDateTime.now(), RolName.ROLE_ADMIN);
		newsEntity = new NewsEntity(idNews, "Se encontraron", "Algo de texto", "miImagen.jpg", categoryEntity,
				"news", false, LocalDateTime.now());
		userEntity = new UserEntity(idUser, "nombreTest", "apellidoTest", "test@email.com", "12345678", "miImagen.jpg", Arrays.asList(roleEntity), LocalDateTime.now(), false);
		commentEntity = new CommentEntity(idComment, userEntity, "cuerpoTest", newsEntity, LocalDateTime.now());
		
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void getComments_commentsExists_returnedListOfBasicCommentDtoStatus200() throws Exception {
		BasicCommentDto commentDto = ObjectMapperUtils.map(commentEntity, BasicCommentDto.class);
		
		when(commentService.findAllOrderByTimestamps()).thenReturn(Arrays.asList(commentDto));
		
		mockMvc.perform(get("/comments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(commentDto)), true));
		
		verify(commentService).findAllOrderByTimestamps();
	}
	
	@Test
	void getComments_commentsNoExists_returnedStatus404() throws Exception {
		when(commentService.findAllOrderByTimestamps()).thenReturn(Arrays.asList());
		
		mockMvc.perform(get("/comments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
		verify(commentService).findAllOrderByTimestamps();
	}
	
	@Test
	void createComment_withValidData_returnedCompleteCommentDtoStatus201() throws Exception {
		EntryCommentDto entryCommentDto = ObjectMapperUtils.map(commentEntity, EntryCommentDto.class);
		CompleteCommentDto completeCommentDto = ObjectMapperUtils.map(commentEntity, CompleteCommentDto.class);
		
		when(userService.existById(idUser)).thenReturn(true);
		when(newsService.existById(idNews)).thenReturn(true);
		when(commentService.saveEntity(entryCommentDto)).thenReturn(completeCommentDto);
		
		mockMvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(entryCommentDto)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(completeCommentDto), true));
		
		verify(userService).existById(idUser);
		verify(newsService).existById(idNews);
		verify(commentService).saveEntity(entryCommentDto);
	}
	
	@Test
	void createComment_withInvalidData_returnedStatus400() throws Exception {
		EntryCommentDto entryCommentDto = ObjectMapperUtils.map(commentEntity, EntryCommentDto.class);
		entryCommentDto.setBody(null);
		
		mockMvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(entryCommentDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
			    .andExpect(result -> assertEquals("There are validation errors.", 
			    		result.getResolvedException().getMessage()));
		
		verify(userService, never()).existById(any());
		verify(newsService, never()).existById(any());
		verify(commentService, never()).saveEntity(any());
	}
	
	@Test
	void createComment_withUserNoExists_returnedStatus404() throws Exception {
		EntryCommentDto entryCommentDto = ObjectMapperUtils.map(commentEntity, EntryCommentDto.class);
		
		when(userService.existById(idUser)).thenReturn(false);
		
		mockMvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(entryCommentDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotExistException))
			    .andExpect(result -> assertEquals("The user with the id: " + idUser + " does not exist", 
			    		result.getResolvedException().getMessage()));
		
		verify(userService).existById(idUser);
		verify(newsService, never()).existById(any());
		verify(commentService, never()).saveEntity(any());
	}
	
	@Test
	void createComment_withNewsNoExists_returnedStatus404() throws Exception {
		EntryCommentDto entryCommentDto = ObjectMapperUtils.map(commentEntity, EntryCommentDto.class);
		
		when(userService.existById(idUser)).thenReturn(true);
		when(newsService.existById(idNews)).thenReturn(false);
		
		mockMvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(entryCommentDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NewsNotExistException))
			    .andExpect(result -> assertEquals("The news with the id: " + idNews + " does not exist", 
			    		result.getResolvedException().getMessage()));
		
		verify(userService).existById(idUser);
		verify(newsService).existById(idNews);
		verify(commentService, never()).saveEntity(any());
	}
	
	@Test
	void editComment_withValidData_returnedCompleteCommentDtoStatus200() throws Exception {
		EditCommentDto editCommentDto = ObjectMapperUtils.map(commentEntity, EditCommentDto.class);
		BasicCommentDto basicCommentDto = ObjectMapperUtils.map(commentEntity, BasicCommentDto.class);
		
		when(commentService.findById(idComment)).thenReturn(Optional.of(commentEntity));
		when(jwtUtils.getToken(any())).thenReturn("tokenTest");
		when(jwtUtils.extractId("tokenTest")).thenReturn(idUser);
		when(userService.findById(idUser)).thenReturn(Optional.of(userEntity));
		when(userService.isAdmin(userEntity)).thenReturn(false);
		when(commentService.editEntity(editCommentDto, commentEntity)).thenReturn(basicCommentDto);
		
		mockMvc.perform(put("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(editCommentDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(basicCommentDto), true));
		
		verify(commentService).findById(idComment);
		verify(jwtUtils).getToken(any());
		verify(jwtUtils).extractId("tokenTest");
		verify(userService).findById(idUser);
		verify(userService, never()).isAdmin(any());
		verify(commentService).editEntity(editCommentDto, commentEntity);
	}
	
	@Test
	void editComment_withInvalidData_returnedStatus400() throws Exception {
		EditCommentDto editCommentDto = ObjectMapperUtils.map(commentEntity, EditCommentDto.class);
		editCommentDto.setBody(null);
		
		mockMvc.perform(put("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(editCommentDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
			    .andExpect(result -> assertEquals("There are validation errors.", 
			    		result.getResolvedException().getMessage()));
		
		verify(commentService, never()).findById(any());
		verify(jwtUtils, never()).getToken(any());
		verify(jwtUtils, never()).extractId(any());
		verify(userService, never()).findById(any());
		verify(userService, never()).isAdmin(any());
		verify(commentService, never()).editEntity(any(), any());
	}
	
	@Test
	void editComment_commentNoExists_returnedStatus404() throws Exception {
		EditCommentDto editCommentDto = ObjectMapperUtils.map(commentEntity, EditCommentDto.class);
		
		when(commentService.findById(idComment)).thenReturn(Optional.empty());
		
		mockMvc.perform(put("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(editCommentDto)))
				.andExpect(status().isNotFound());
		
		verify(commentService).findById(idComment);
		verify(jwtUtils, never()).getToken(any());
		verify(jwtUtils, never()).extractId(any());
		verify(userService, never()).findById(any());
		verify(userService, never()).isAdmin(any());
		verify(commentService, never()).editEntity(any(), any());
	}
	
	@Test
	void editComment_userIsNotAdminOrOwner_returnedStatus401() throws Exception {
		UserEntity usuarioNoAdminDueño = new UserEntity("44564654654", "nombreTest", "apellidoTest", "test@email.com", "12345678", "miImagen.jpg", Arrays.asList(roleEntity), LocalDateTime.now(), false);
		commentEntity.setUserId(usuarioNoAdminDueño);
		EditCommentDto editCommentDto = ObjectMapperUtils.map(commentEntity, EditCommentDto.class);
		
		when(commentService.findById(idComment)).thenReturn(Optional.of(commentEntity));
		when(jwtUtils.getToken(any())).thenReturn("tokenTest");
		when(jwtUtils.extractId("tokenTest")).thenReturn(idUser);
		when(userService.findById(idUser)).thenReturn(Optional.of(userEntity));
		when(userService.isAdmin(userEntity)).thenReturn(false);
		
		mockMvc.perform(put("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(editCommentDto)))
				.andExpect(status().isUnauthorized());
		
		verify(commentService).findById(idComment);
		verify(jwtUtils).getToken(any());
		verify(jwtUtils).extractId("tokenTest");
		verify(userService).findById(idUser);
		verify(userService).isAdmin(userEntity);
		verify(commentService, never()).editEntity(any(), any());
	}
	
	@Test
	void editComment_userIsAdminAndNotOwner_returnedCompleteCommentDtoStatus200() throws Exception {
		UserEntity usuarioNoAdminDueño = new UserEntity("44564654654", "nombreTest", "apellidoTest", "test@email.com", "12345678", "miImagen.jpg", Arrays.asList(roleEntity), LocalDateTime.now(), false);
		commentEntity.setUserId(usuarioNoAdminDueño);
		EditCommentDto editCommentDto = ObjectMapperUtils.map(commentEntity, EditCommentDto.class);
		BasicCommentDto basicCommentDto = ObjectMapperUtils.map(commentEntity, BasicCommentDto.class);
		
		when(commentService.findById(idComment)).thenReturn(Optional.of(commentEntity));
		when(jwtUtils.getToken(any())).thenReturn("tokenTest");
		when(jwtUtils.extractId("tokenTest")).thenReturn(idUser);
		when(userService.findById(idUser)).thenReturn(Optional.of(userEntity));
		when(userService.isAdmin(userEntity)).thenReturn(true);
		when(commentService.editEntity(editCommentDto, commentEntity)).thenReturn(basicCommentDto);
		
		mockMvc.perform(put("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(editCommentDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(basicCommentDto), true));
		
		verify(commentService).findById(idComment);
		verify(jwtUtils).getToken(any());
		verify(jwtUtils).extractId("tokenTest");
		verify(userService).findById(idUser);
		verify(userService).isAdmin(userEntity);
		verify(commentService).editEntity(editCommentDto, commentEntity);
	}
	
	@Test
	void deleteComment_withValidData_returnedBasicCommentDtoStatus204() throws Exception{
		when(commentService.findById(idComment)).thenReturn(Optional.of(commentEntity));
		when(jwtUtils.getToken(any())).thenReturn("tokenTest");
		when(jwtUtils.extractId("tokenTest")).thenReturn(idUser);
		when(userService.findById(idUser)).thenReturn(Optional.of(userEntity));
		when(userService.isAdmin(userEntity)).thenReturn(false);
		doNothing().when(commentService).delete(commentEntity);
		
		mockMvc.perform(delete("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNoContent());
		
		verify(commentService).findById(idComment);
		verify(jwtUtils).getToken(any());
		verify(jwtUtils).extractId("tokenTest");
		verify(userService).findById(idUser);
		verify(userService, never()).isAdmin(userEntity);
		verify(commentService).delete(commentEntity);
	}
	
	@Test
	void deleteComment_commentNotExists_returnedStatus404() throws Exception{
		when(commentService.findById(idComment)).thenReturn(Optional.empty());
		
		mockMvc.perform(delete("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		
		verify(commentService).findById(idComment);
		verify(jwtUtils, never()).getToken(any());
		verify(jwtUtils, never()).extractId(any());
		verify(userService, never()).findById(any());
		verify(userService, never()).isAdmin(any());
		verify(commentService, never()).delete(any());
	}
	
	@Test
	void deleteComment_userIsNotAdminOrOwner_returnedStatus401() throws Exception {
		UserEntity usuarioNoAdminDueño = new UserEntity("44564654654", 
				"nombreTest", "apellidoTest", "test@email.com", "12345678", "miImagen.jpg", Arrays.asList(roleEntity), LocalDateTime.now(), false);
		commentEntity.setUserId(usuarioNoAdminDueño);
		
		when(commentService.findById(idComment)).thenReturn(Optional.of(commentEntity));
		when(jwtUtils.getToken(any())).thenReturn("tokenTest");
		when(jwtUtils.extractId("tokenTest")).thenReturn(idUser);
		when(userService.findById(idUser)).thenReturn(Optional.of(userEntity));
		when(userService.isAdmin(userEntity)).thenReturn(false);
		
		mockMvc.perform(delete("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isUnauthorized());
		
		verify(commentService).findById(idComment);
		verify(jwtUtils).getToken(any());
		verify(jwtUtils).extractId("tokenTest");
		verify(userService).findById(idUser);
		verify(userService).isAdmin(userEntity);
		verify(commentService, never()).delete(any());
	}
	
	@Test
	void deleteComment_userIsAdminAndNotOwner_returnedCompleteCommentDtoStatus200() throws Exception {
		UserEntity usuarioNoAdminDueño = new UserEntity("44564654654", 
				"nombreTest", "apellidoTest", "test@email.com", "12345678", "miImagen.jpg", Arrays.asList(roleEntity), LocalDateTime.now(), false);
		commentEntity.setUserId(usuarioNoAdminDueño);
		
		when(commentService.findById(idComment)).thenReturn(Optional.of(commentEntity));
		when(jwtUtils.getToken(any())).thenReturn("tokenTest");
		when(jwtUtils.extractId("tokenTest")).thenReturn(idUser);
		when(userService.findById(idUser)).thenReturn(Optional.of(userEntity));
		when(userService.isAdmin(userEntity)).thenReturn(true);
		doNothing().when(commentService).delete(commentEntity);
		
		mockMvc.perform(delete("/comments/" + idComment).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(commentService).findById(idComment);
		verify(jwtUtils).getToken(any());
		verify(jwtUtils).extractId("tokenTest");
		verify(userService).findById(idUser);
		verify(userService).isAdmin(userEntity);
		verify(commentService).delete(commentEntity);
	}
	
	@Test
	void getCommentsOfPost_withCommentAndNewsExists_returnedListOfBasicCommentDtoStatus200() throws Exception {
		BasicCommentDto commentDto = ObjectMapperUtils.map(commentEntity, BasicCommentDto.class);
		
		when(newsService.existById(idNews)).thenReturn(true);
		when(commentService.findAllByNewsId(idNews)).thenReturn(Arrays.asList(commentDto));
		
		mockMvc.perform(get("/post/" + idNews + "/comments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(commentDto)), true));
		
		verify(newsService).existById(idNews);
		verify(commentService).findAllByNewsId(idNews);
	}
	
	@Test
	void getCommentsOfPost_withNewNotExists_returnedStatus404() throws Exception {
		when(newsService.existById(idNews)).thenReturn(false);
		
		mockMvc.perform(get("/post/" + idNews + "/comments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NewsNotExistException))
			    .andExpect(result -> assertEquals("The news with the id: " + idNews + " does not exist", 
			    		result.getResolvedException().getMessage()));
		
		verify(newsService).existById(idNews);
		verify(commentService, never()).findAllByNewsId(any());
	}
	
	@Test
	void getCommentsOfPost_withNewExistsAndCommentNotExists_returnedStatus404() throws Exception {
		when(newsService.existById(idNews)).thenReturn(true);
		when(commentService.findAllByNewsId(idNews)).thenReturn(Arrays.asList());
		
		mockMvc.perform(get("/post/" + idNews + "/comments").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
		verify(newsService).existById(idNews);
		verify(commentService).findAllByNewsId(idNews);
	}
}
