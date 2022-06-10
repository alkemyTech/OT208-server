package com.alkemy.ong.services;

import java.util.List;

import com.alkemy.ong.models.CommentEntity;

public interface CommentService extends BasicService<CommentEntity, String> {

	List<CommentEntity> findAllOrderByTimestamps();

}
