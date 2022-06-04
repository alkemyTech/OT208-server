package com.alkemy.ong.dto.request.news;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryNewsDto {
	
	@NotBlank(message = "The name cannot be empty or null")
	@Size(max = 50, message = "The maximum size for the name is fifty characters")
    private String name;

	@NotBlank(message = "The content cannot be empty or null")
	@Size(max = 50, message = "The maximum size for the content is fifty characters")
    private String content;

	@NotBlank(message = "The category id cannot be empty or null")
	@Size(max = 36, message = "The maximum size for the category id is thirty six characters")
    private String categoryIdId;
}
