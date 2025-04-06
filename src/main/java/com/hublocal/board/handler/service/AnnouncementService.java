package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.Announcement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementService {

    List<Announcement> listAnnouncements();
    Optional<Announcement> getAnnouncementById(String id);
    Announcement saveAnnouncement(Announcement announcement);
    Announcement updateAnnouncement(String id, Announcement announcement);
    void deleteAnnouncement(UUID id);

}
