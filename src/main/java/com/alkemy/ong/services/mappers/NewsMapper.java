package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.request.news.EntryNewsDto;
import com.alkemy.ong.models.NewsEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsMapper {

	private final ModelMapper mapper;
	
	public NewsEntity mapperEntryNewsDtoToNewsEntity(EntryNewsDto entryNewsDto, NewsEntity newsEntity) {
		mapper.map(entryNewsDto, newsEntity);
		return newsEntity;
	}
}
