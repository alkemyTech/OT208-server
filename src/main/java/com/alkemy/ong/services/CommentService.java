package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.comment.EditCommentDto;
import com.alkemy.ong.dto.request.comment.EntryCommentDto;
import com.alkemy.ong.dto.response.comment.BasicCommentDto;
import com.alkemy.ong.dto.response.comment.CompleteCommentDto;
import com.alkemy.ong.models.CommentEntity;

import java.util.List;

public interface CommentService extends BasicService<CommentEntity, String> {

    List<BasicCommentDto> findAllOrderByTimestamps();

    List<BasicCommentDto> findAllByNewsId(String id);

    CompleteCommentDto saveEntity(EntryCommentDto entryCommentDto);

    BasicCommentDto editEntity(EditCommentDto editCommentDto, CommentEntity comment);

}
