package com.hublocal.board.handler.repository;

import com.hublocal.board.handler.entities.Category;
import com.hublocal.board.handler.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findByAnnouncementIdAndId(String announcementId, UUID commentId);

}
