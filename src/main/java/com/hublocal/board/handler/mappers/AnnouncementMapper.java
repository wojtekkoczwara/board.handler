package com.hublocal.board.handler.mappers;

import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.model.AnnouncementDto;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface AnnouncementMapper {


    Announcement toEntity(AnnouncementDto announcementDto);

    AnnouncementDto toDto(Announcement announcement);
}
