package com.alkemy.ong.dto.request.comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCommentDto {
	
	@NotEmpty(message = "")
	@Size(max = 255, message = "")
	private String body;
}
