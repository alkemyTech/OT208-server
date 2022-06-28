package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.comment.EditCommentDto;
import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final NewsService newsService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Endpoint to list all comments",
            description = "It provides the necessary mechanism to be able to list all the existing comments ordered by creation date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = BasicCommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})
    
    @GetMapping("/comments")
    public ResponseEntity<List<BasicCommentDto>> getComments() {
        List<BasicCommentDto> comments = commentService.findAllOrderByTimestamps();

        if (comments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comments);
    }
    
    @Operation(summary = "Endpoint to create a comment.",
            description = "It provides the necessary mechanism to be able to create a new comment.",
            requestBody = @RequestBody)

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",  content = @Content(schema = @Schema(implementation = CompleteCommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ValidationException.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})

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
        CompleteCommentDto a = commentService.saveEntity(entryCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }
    
    @Operation(summary = "Endpoint to edit a comment.",
            description = "It provides the necessary mechanism to be able to edit a comment."
            		+ " It can only be edited by its author or by an administrator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = BasicCommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ValidationException.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})

    @PutMapping("/comments/{id}")
    public ResponseEntity<BasicCommentDto> editComment(
    		@Parameter(description = "Id of the comment to be edited", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
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
        String idUser = jwtUtils.extractId(jwtUtils.getToken(request));
        UserEntity user = userService.findById(idUser).get();

        if (!user.equals(comment.getUserId()) && !userService.isAdmin(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(commentService.editEntity(editCommentDto, comment));
    }
    
    @Operation(summary = "Endpoint to delete a comment.",
    		description = "It provides the necessary mechanism to be able to eliminate a comment based on its id."
    				+ " It can only be edited by its author or by an administrator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BasicCommentDto> deleteComment(
    		@Parameter(description = "Id of the comment to be edited", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
    		@PathVariable String id, 
    		@Parameter(hidden = true) HttpServletRequest request) {
        Optional<CommentEntity> commentOp = commentService.findById(id);

        if (commentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CommentEntity comment = commentOp.get();
        String idUser = jwtUtils.extractId(jwtUtils.getToken(request));
        UserEntity user = userService.findById(idUser).get();

        if (!user.equals(comment.getUserId()) && !userService.isAdmin(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        commentService.delete(comment);

        return ResponseEntity.noContent().build();
    }
    
    

    @Operation(summary = "Endpoint that returns a list of comments from the id of a news.",
    		description = "It provides the necessary mechanism to be able to obtain a list of comments of a certain news from its id.")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = BasicCommentDto.class)))
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema()))
    
    @GetMapping("/post/{id}/comments")
    public ResponseEntity<List<BasicCommentDto>> getCommentsOfPost(
    		@Parameter(description = "Id of the news you want to search for to list its comments.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
    		@PathVariable String id) {

        if (!newsService.existById(id)) {
            throw new NewsNotExistException(id);
        }
        List<BasicCommentDto> comments = commentService.findAllByNewsId(id);

        if (comments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comments);
    }
}
