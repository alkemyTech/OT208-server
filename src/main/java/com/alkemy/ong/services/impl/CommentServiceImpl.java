package com.alkemy.ong.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.request.comment.EditCommentDto;
import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.comment.BasicCommentDto;
import com.alkemy.ong.dto.response.comment.CompleteCommentDto;
import com.alkemy.ong.models.CommentEntity;
import com.alkemy.ong.repositories.ICommentRepository;
import com.alkemy.ong.services.CommentService;
import com.alkemy.ong.utils.ObjectMapperUtils;

@Service
public class CommentServiceImpl extends BasicServiceImpl<CommentEntity, String, ICommentRepository>
		implements CommentService {

	@Autowired
	public CommentServiceImpl(ICommentRepository repository) {
		super(repository);
	}

	@Override
	public List<BasicCommentDto> findAllOrderByTimestamps() {
		return ObjectMapperUtils.mapAll(this.repository.findAllByOrderByTimestampsDesc(), BasicCommentDto.class);
	}

	@Override
	public List<BasicCommentDto> findAllByNewsId(String id) {
		return ObjectMapperUtils.mapAll( this.repository.findAllByNewsId(id), BasicCommentDto.class);
	}

	@Override
	public CompleteCommentDto saveEntity(EntryCommentDto entryCommentDto) {
		CommentEntity comment = save(ObjectMapperUtils.map(entryCommentDto, CommentEntity.class));

		return ObjectMapperUtils.map(comment, CompleteCommentDto.class);
	}

	@Override
	public BasicCommentDto editEntity(EditCommentDto editCommentDto, CommentEntity comment) {
		CommentEntity commentEntity =  edit(ObjectMapperUtils.map(editCommentDto, comment));
		
		return ObjectMapperUtils.map(commentEntity, BasicCommentDto.class);
	}

}
