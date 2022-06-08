package com.alkemy.ong.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.comment.CompleteCommentDto;
import com.alkemy.ong.exeptions.NewsNotExistException;
import com.alkemy.ong.exeptions.UserNotExistException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.mappers.CommentMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;
	private final CommentMapper commentMapper;
	private final UserService userService;
	private final NewsService newsService;

	@PostMapping
	private ResponseEntity<CompleteCommentDto> createComment(
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
		CommentEntity commentEntity = commentMapper.entryCommentDtoToEntity(entryCommentDto);
		commentEntity = commentService.save(commentEntity);

		return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.entityToCompleteCommentDto(commentEntity));
	}
}
