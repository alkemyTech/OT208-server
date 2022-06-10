package com.alkemy.ong.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.response.comment.BasicCommentDto;
import com.alkemy.ong.models.CommentEntity;
import com.alkemy.ong.services.CommentService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;

	@GetMapping
	public ResponseEntity<List<BasicCommentDto>> getComments() {
		List<CommentEntity> comments = commentService.findAllOrderByTimestamps();

		if (comments.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<BasicCommentDto> commentsDto = ObjectMapperUtils.mapAll(comments, BasicCommentDto.class);

		return ResponseEntity.ok(commentsDto);
	}
}
