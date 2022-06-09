package com.alkemy.ong.dto.response.news;

import com.alkemy.ong.models.CategoryEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicNewsDto {

	private String id;
	private String name;
	private String content;
	private String image;
	private CategoryEntity categoryId;
	
}
