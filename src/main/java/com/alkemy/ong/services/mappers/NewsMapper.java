package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.response.news.BasicNewsEntity;
import com.alkemy.ong.models.NewsEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsMapper {
	
	private final ModelMapper mapper;
	
	public BasicNewsEntity mapperBasicNewsEntityToNewsEntity(NewsEntity newsEntity) {
		return mapper.map(newsEntity, BasicNewsEntity.class);
	}
}
