package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllByAnnouncement(String id);

    List<CommentDto> findAllByUser(String id);

    Optional<CommentDto> findByAnnouncementAndCommentId(String id, String commentId);

    CommentDto saveComment(String userId, String announcementId, CommentDto commentDto);

    void deleteComment(String userId, String announcementId, String commentId);
}
