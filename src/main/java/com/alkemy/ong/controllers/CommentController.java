package com.alkemy.ong.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.request.comment.EditCommentDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.security.enums.RolName;
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
	private final JwtUtils jwtUtils;
	
	@PutMapping("/{id}")
	public ResponseEntity<BasicCommentDto> editComment(
			@PathVariable String id,
			EditCommentDto editCommentDto,
			Errors errors,
			HttpServletRequest request){
		
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getFieldErrors());
		}
		if (commentService.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		String token = jwtUtils.getToken(request);
		String idUser = jwtUtils.extractId(token);
		CommentEntity comment = commentService.findById(id);
		UserEntity user = userService.findById("asdas").get();
		
		//Probar que funcione con debug
		boolean isAdmin = user.getRoleIds().stream()
				.anyMatch(rol -> rol.getRolName().equals(RolName.ROLE_ADMIN));
		
		if (!user.getId().equals(comment.getUserId()) || !isAdmin) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		comment = commentMapper.editCommentDtoToEntity(editCommentDto, comment);
		comment = commentService.edit(comment);
		
		return ResponseEntity.ok(commentMapper.entityToBasicCommentDto(comment));
	}
}
