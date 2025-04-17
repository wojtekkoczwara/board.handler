package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {

    List<Announcement> listAnnouncements();
    Optional<Announcement> getAnnouncementById(String id);
    Announcement saveAnnouncement(Announcement announcement, String userId);
    Announcement updateAnnouncement(String id, Announcement announcement, String userId);
    void deleteAnnouncement(String id, String userId);

}
