package com.alkemy.ong.dto.response.news;

import java.time.LocalDateTime;

import com.alkemy.ong.models.CategoryEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicNewsEntity {

	private String id;
	private String name;
	private String content;
	private String image;
	private CategoryEntity categoryId;
	private Boolean softDelete;
	private LocalDateTime timestamps;
	
}
