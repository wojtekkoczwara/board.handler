package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.AnnouncementDto;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {

    List<AnnouncementDto> listAnnouncements();
    Optional<AnnouncementDto> getAnnouncementById(String id);
    AnnouncementDto saveAnnouncement(AnnouncementDto announcementDto, String userId);
    AnnouncementDto updateAnnouncement(String id, AnnouncementDto announcementDto, String userId);
    void deleteAnnouncement(String id, String userId);

}
