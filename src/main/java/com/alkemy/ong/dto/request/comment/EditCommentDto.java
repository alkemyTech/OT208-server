package com.alkemy.ong.dto.request.comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCommentDto {
	
	@NotEmpty(message = "The body cannot be empty or null")
	@Size(max = 255, message = "The maximum size for the body is thirty six characters")
	private String body;
}
