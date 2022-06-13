package com.alkemy.ong.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.request.comment.EditCommentDto;
import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.comment.BasicCommentDto;
import com.alkemy.ong.dto.response.comment.CompleteCommentDto;
import com.alkemy.ong.exeptions.NewsNotExistException;
import com.alkemy.ong.exeptions.UserNotExistException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.CommentEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.CommentService;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final UserService userService;
	private final NewsService newsService;
	private final JwtUtils jwtUtils;

	@GetMapping("/comments")
	public ResponseEntity<List<BasicCommentDto>> getComments() {
		List<CommentEntity> comments = commentService.findAllOrderByTimestamps();

		if (comments.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<BasicCommentDto> commentsDto = ObjectMapperUtils.mapAll(comments, BasicCommentDto.class);

		return ResponseEntity.ok(commentsDto);
	}

	@PostMapping("/comments")
	public ResponseEntity<CompleteCommentDto> createComment(
			@Valid @RequestBody EntryCommentDto entryCommentDto,
			Errors errors) {

		if (errors.hasErrors()) {
			throw new ValidationException(errors.getFieldErrors());
		}
		if (!userService.existById(entryCommentDto.getUserIdId())) {
			throw new UserNotExistException(entryCommentDto.getUserIdId());
		}
		if (!newsService.existById(entryCommentDto.getNewsIdId())) {
			throw new NewsNotExistException(entryCommentDto.getNewsIdId());
		}
		CommentEntity commentEntity = ObjectMapperUtils.map(entryCommentDto, CommentEntity.class);
		commentEntity = commentService.save(commentEntity);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ObjectMapperUtils.map(commentEntity, CompleteCommentDto.class));
	}

	@PutMapping("/comments/{id}")
	public ResponseEntity<BasicCommentDto> editComment(
			@PathVariable String id, 
			@Valid @RequestBody EditCommentDto editCommentDto,
			Errors errors, HttpServletRequest request) {

		if (errors.hasErrors()) {
			throw new ValidationException(errors.getFieldErrors());
		}
		Optional<CommentEntity> commentOp = commentService.findById(id);
		
		if (!commentOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		CommentEntity comment = commentOp.get();
		String token = jwtUtils.getToken(request);
		String idUser = jwtUtils.extractId(token);
		UserEntity user = userService.findById(idUser).get();

		if (!user.equals(comment.getUserId()) || !userService.isAdmin(user)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		comment = ObjectMapperUtils.map(editCommentDto, comment);
		comment = commentService.edit(comment);
		
		return ResponseEntity.ok(ObjectMapperUtils.map(comment, BasicCommentDto.class));
	}

	@DeleteMapping("/comments/{id}")
	public ResponseEntity<BasicCommentDto> deleteComment(@PathVariable String id, HttpServletRequest request){
		Optional<CommentEntity> commentOp = commentService.findById(id);
		
		if(commentOp.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		CommentEntity comment = commentOp.get();
		String token = jwtUtils.getToken(request);
		String idUser = jwtUtils.extractId(token);
		UserEntity user = userService.findById(idUser).get();
		
		if (!user.equals(comment.getUserId()) || !userService.isAdmin(user)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		commentService.delete(comment);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/post/{id}/comments")
	public ResponseEntity<List<BasicCommentDto>> getCommentsOfPost(@PathVariable String id){
		
		if(!newsService.existById(id)) {
			throw new NewsNotExistException(id);
		}
		List<CommentEntity> comments = commentService.findAllByNewsId(id);

		if (comments.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(ObjectMapperUtils.mapAll(comments, BasicCommentDto.class));
	}
}
