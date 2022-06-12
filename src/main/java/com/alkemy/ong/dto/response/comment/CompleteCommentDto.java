package com.alkemy.ong.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteCommentDto {

	@JsonProperty(value = "userId")
	private String userIdId;
	private String body;
	@JsonProperty(value = "newsId")
	private String newsIdId;
}
