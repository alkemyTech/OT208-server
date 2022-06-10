package com.alkemy.ong.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.security.enums.RolName;
import com.alkemy.ong.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

	private final CommentsService commentsService;
	private final UserService userService;
	private final JwtUtils jwtUtils;
	
	@DeleteMapping("/{id}")
	public ResponseEntity<BasicCommentDto> deleteComment(@PathVariable String id, HttpServletRequest request){
		Optional<CommentEntity> comment = commentsService.findById(id);
		
		if(comment.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		String token = jwtUtils.getToken(request);
		String idUser = jwtUtils.extractId(token);
		UserEntity user = userService.findById(idUser).get();
		boolean isAdmin = user.getRoleIds().stream()
				.anyMatch(rol -> rol.getRolName().equals(RolName.ROLE_ADMIN));
		
		if (!user.getId().equals(comment.getUserId()) || !isAdmin) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		commentsService.delete(comment.get());
		
		return ResponseEntity.noContent().build();
	}
}
