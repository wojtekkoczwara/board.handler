package com.hublocal.board.handler.mappers;

import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.model.AnnouncementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper
public interface AnnouncementMapper {


    @Mapping(ignore = true, target = "comments")
    Announcement toEntity(AnnouncementDto announcementDto);

    AnnouncementDto toDto(Announcement announcement);
}
