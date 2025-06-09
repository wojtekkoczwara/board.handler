package com.hublocal.board.handler.controller;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.CommentDto;
import com.hublocal.board.handler.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hublocal.board.handler.controller.AnnouncementController.ANNOUNCEMENT_ID;
import static com.hublocal.board.handler.controller.AnnouncementController.ANNOUNCEMENT_PATH;
import static com.hublocal.board.handler.controller.UserController.USERS_PATH;
import static com.hublocal.board.handler.controller.UserController.USER_ID;
import static com.hublocal.board.handler.utils.CommentUtils.CommentLogic.getComment;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    public static final String COMMENTS = "/comments";
    public static final String COMMENTS_FOR_ANNOUNCEMENT = ANNOUNCEMENT_PATH + ANNOUNCEMENT_ID + COMMENTS;
    public static final String COMMENTS_FOR_USER = USERS_PATH + USER_ID + COMMENTS;
    public static final String COMMENTS_ID = COMMENTS_FOR_ANNOUNCEMENT + "/{commentId}";

    private final CommentService commentService;

    @GetMapping(COMMENTS_FOR_ANNOUNCEMENT)
    public List<CommentDto> findAllByAnnouncement(@PathVariable String announcementId) {
        return commentService.findAllByAnnouncement(announcementId);
    }

    @GetMapping(COMMENTS_FOR_USER)
    public List<CommentDto> findAllByUser(@PathVariable String userId) {
        return commentService.findAllByUser(userId);
    }

    @GetMapping(COMMENTS_ID)
    public CommentDto getCommentById(@PathVariable String announcementId, @PathVariable String commentId) {
        return commentService.findByAnnouncementAndCommentId(announcementId, commentId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(COMMENTS_FOR_ANNOUNCEMENT)
    public ResponseEntity<CommentDto> createComment(@RequestHeader String userId, @PathVariable String announcementId,
                                                    @Validated @RequestBody CommentDto commentDto) {

        CommentDto commentDto1 = commentService.saveComment(userId, announcementId, commentDto);
        String commentId = commentDto1.getId().toString();
        return new ResponseEntity<>(getComment(commentService, announcementId, commentId), HttpStatus.CREATED);
    }

    @DeleteMapping(COMMENTS_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestHeader String userId, @PathVariable String announcementId, @PathVariable String commentId) {
        commentService.deleteComment(userId, announcementId, commentId);
    }
}
