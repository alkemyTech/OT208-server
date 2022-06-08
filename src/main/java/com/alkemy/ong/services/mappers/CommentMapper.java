package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.response.comment.BasicCommentDto;
import com.alkemy.ong.models.CommentEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentMapper {

	private final ModelMapper mapper;
	
	public BasicCommentDto entityToBasicCommentDto(CommentEntity commentEntity) {
		return mapper.map(commentEntity, BasicCommentDto.class);
	}
	
}
