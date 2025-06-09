package com.hublocal.board.handler.utils.CommentUtils;

import com.hublocal.board.handler.entities.Comment;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.CommentDto;
import com.hublocal.board.handler.repository.CommentRepository;
import com.hublocal.board.handler.service.CommentService;

import java.util.UUID;

public class CommentLogic {
    public static CommentDto getComment(CommentService commentService, String announcementId, String commentId) {
        return commentService.findByAnnouncementAndCommentId(announcementId, commentId)
                .orElseThrow(NotFoundException::new);
    }

    public static Comment getComment(CommentRepository commentRepository, String commentId) {
        return commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(NotFoundException::new);
    }
}
