package com.alkemy.ong.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompleteCommentDto {

	@JsonProperty(value = "userId")
	private String userIdId;
	private String body;
	@JsonProperty(value = "newsId")
	private String newsIdId;
}
