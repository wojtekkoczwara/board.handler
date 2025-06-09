package com.hublocal.board.handler.mappers;

import com.hublocal.board.handler.entities.Comment;
import com.hublocal.board.handler.model.CommentDto;
import org.mapstruct.*;

@Mapper
public interface CommentMapper {

    @Mapping(ignore = true, target = "announcementId")
    @Mapping(ignore = true, target = "userId")
    Comment toEntity(CommentDto commentDto);

    CommentDto toDto(Comment comment);
}