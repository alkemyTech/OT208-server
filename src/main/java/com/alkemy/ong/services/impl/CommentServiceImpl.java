package com.alkemy.ong.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.ong.models.CommentEntity;
import com.alkemy.ong.repositories.ICommentRepository;
import com.alkemy.ong.services.CommentService;

@Service
public class CommentServiceImpl extends BasicServiceImpl<CommentEntity, String, ICommentRepository> implements CommentService {

	@Autowired
	public CommentServiceImpl(ICommentRepository repository) {
		super(repository);
	}

	@Override
	public List<CommentEntity> findAllOrderByTimestamps() {
		return this.repository.findAllByOrderByTimestampsDesc();
	}

}
