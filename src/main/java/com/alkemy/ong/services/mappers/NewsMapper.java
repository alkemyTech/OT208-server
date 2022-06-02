package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.response.news.BasicNewsDto;
import com.alkemy.ong.models.NewsEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsMapper {
	
	private final ModelMapper mapper;
	
	public BasicNewsDto mapperNewsEntityToBasicNewsDto(NewsEntity newsEntity) {
		return mapper.map(newsEntity, BasicNewsDto.class);
	}
}
