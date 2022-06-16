package com.alkemy.ong.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.CommentEntity;

public interface ICommentRepository extends JpaRepository<CommentEntity, String>{

	List<CommentEntity> findAllByOrderByTimestampsDesc();
	
}
