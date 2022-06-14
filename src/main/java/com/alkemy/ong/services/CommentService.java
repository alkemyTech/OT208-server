package com.alkemy.ong.services;

import java.util.Collection;
import java.util.List;

import com.alkemy.ong.models.CommentEntity;

public interface CommentService extends BasicService<CommentEntity, String> {

	List<CommentEntity> findAllOrderByTimestamps();

	List<CommentEntity> findAllByNewsId(String id);

	<D, T> D map(final T entity, Class<D> outClass);
	
	<S, D> D map(final S source, D destination);
	
	<D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass);

}
