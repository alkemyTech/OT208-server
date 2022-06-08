package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.comment.CompleteCommentDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentMapper {

	private final ModelMapper mapper;

	public CommentEntity entryCommentDtoToEntity(EntryCommentDto entryCommentDto) {
		return mapper.map(entryCommentDto, CommentEntity.class);
	}
	
	public CompleteCommentDto entityToCompleteCommentDto(CommentEntity commentEntity) {
		return mapper.map(commentEntity, CompleteCommentDto.class);
	}

}
