package com.hublocal.board.handler.service;

import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.entities.Comment;
import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.mappers.CommentMapper;
import com.hublocal.board.handler.entities.Users;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.CommentDto;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.repository.CommentRepository;
import com.hublocal.board.handler.repository.UserRepository;
import com.hublocal.board.handler.utils.CommentUtils.CommentLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> findAllByAnnouncement(String id) {
        return announcementRepository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new)
                .getComments().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findAllByUser(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new)
                .getComments().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> findByAnnouncementAndCommentId(String id, String commentId) {
        return announcementRepository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new).getComments()
                .stream().filter(comment -> comment.getId().toString().equals(commentId))
                .findFirst().map(commentMapper::toDto);
    }

    @Override
    public CommentDto saveComment(String userId, String announcementId, CommentDto commentDto) {

        Users userDb = userRepository.findById(UUID.fromString(userId)).orElseThrow(NotFoundException::new);
        Announcement announcementDb = announcementRepository.findById(UUID.fromString(announcementId)).orElseThrow(NotFoundException::new);

        Comment commentDb = commentMapper.toEntity(commentDto);
        Comment commentSaved = commentRepository.save(commentDb);
        announcementDb.getComments().add(commentSaved);
        userDb.getComments().add(commentSaved);
        announcementRepository.save(announcementDb);
        userRepository.save(userDb);
        return commentMapper.toDto(commentSaved);
    }

    @Override
    public void deleteComment(String userId, String announcementId, String commentId) {
        //check if comment exist
        Comment commentDb = CommentLogic.getComment(commentRepository, commentId);

        UUID id = UUID.fromString(commentId);

        Users userDb = userRepository.findById(UUID.fromString(userId)).orElseThrow(NotFoundException::new);
        Announcement announcementDb = announcementRepository.findById(UUID.fromString(announcementId)).orElseThrow(NotFoundException::new);

        //scenario: userId is comment-creator id & announcement is exist & has the comment
        if(userDb.getComments().contains(commentDb)) {
            userDb.getComments().remove(commentDb);

            if(announcementDb.getComments().contains(commentDb)) {
                announcementDb.getComments().remove(commentDb);
                userRepository.save(userDb);
                announcementRepository.save(announcementDb);
                commentRepository.deleteById(id);
                return;
            } else {
                throw new CustomException(String.format("Announcement with id: '%s' does not contain comment: '%s'.",
                        announcementId, commentId));
            }
        }

        //scenario: userId is announcement owner & announcement has the comment
        Comment commentDb2 = commentRepository.findByAnnouncementIdAndId(announcementId, id).orElseThrow(() -> new
                CustomException(String.format("Announcement with id: '%s' does not contain comment: '%s'.",announcementId, commentId)));
        if(userDb.getAnnouncements().contains(announcementDb)) {
            announcementDb.getComments().remove(commentDb2);
            announcementRepository.save(announcementDb);
            commentRepository.deleteById(id);
            return;
        }

        throw new CustomException(String.format("User with id: '%s' has no permission to delete comment: '%s'. Only " +
                            "comment creator or announcement owner has right to delete comment",
                    userId, commentId));
    }
}
