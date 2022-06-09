package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.request.comment.EditCommentDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentMapper {

	private final ModelMapper mapper;

	public CommentEntity editCommentDtoToEntity(EditCommentDto editCommentDto, CommentEntity comment) {
		mapper.map(editCommentDto, comment);
		return comment;
	}
}
