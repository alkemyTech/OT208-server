package com.alkemy.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.CommentEntity;

public interface ICommentRepository extends JpaRepository<CommentEntity, String>{
	
}
