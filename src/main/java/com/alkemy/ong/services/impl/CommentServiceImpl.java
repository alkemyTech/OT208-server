package com.alkemy.ong.services.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.ong.models.CommentEntity;
import com.alkemy.ong.repositories.ICommentRepository;
import com.alkemy.ong.services.CommentService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;

@Service
public class CommentServiceImpl extends BasicServiceImpl<CommentEntity, String, ICommentRepository> implements CommentService {

	@Autowired
	public CommentServiceImpl(ICommentRepository repository) {
		super(repository);
	}

	@Override
	public List<CommentEntity> findAllOrderByTimestamps() {
		return this.repository.findAllByOrderByTimestampsAsc();
	}

	@Override
	public List<CommentEntity> findAllByNewsId(String id) {
		return this.repository.findAllByNewsId(id);
	}

	@Override
	public <D, T> D map(T entity, Class<D> outClass) {
		return ObjectMapperUtils.map(entity, outClass);
	}

	@Override
	public <S, D> D map(S source, D destination) {
		return ObjectMapperUtils.map(source, destination);
	}

	@Override
	public <D, T> List<D> mapAll(Collection<T> entityList, Class<D> outCLass) {
		return ObjectMapperUtils.mapAll(entityList, outCLass);
	}

}
