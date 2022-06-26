package com.alkemy.ong.repositories;

import com.alkemy.ong.models.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICommentRepository extends JpaRepository<CommentEntity, String> {

    List<CommentEntity> findAllByOrderByTimestampsDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM comments WHERE news_id = :id")
    List<CommentEntity> findAllByNewsId(String id);

}
