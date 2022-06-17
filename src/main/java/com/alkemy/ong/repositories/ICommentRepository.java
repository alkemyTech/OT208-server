package com.alkemy.ong.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alkemy.ong.models.CommentEntity;

public interface ICommentRepository extends JpaRepository<CommentEntity, String>{

	List<CommentEntity> findAllByOrderByTimestampsDesc();
	
	@Query(nativeQuery = true, value = "SELECT * FROM comments WHERE news_id = :id")
	List<CommentEntity> findAllByNewsId(String id);
	
}
