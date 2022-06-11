package com.alkemy.ong.dto.request.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryCommentDto {

	@NotBlank
	@Size(max = 36)
	@JsonProperty(value = "userId")
	private String userIdId;
	
	@NotBlank
	@Size(max = 255)
	private String body;
	
	@NotBlank
	@Size(max = 36)
	@JsonProperty(value = "userId")
	private String newsIdId;
}
