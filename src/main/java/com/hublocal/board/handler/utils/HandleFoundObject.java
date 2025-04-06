package com.hublocal.board.handler.utils;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.service.AnnouncementService;

import java.util.Objects;

public class HandleFoundObject {

    public static Announcement getAnnouncement(AnnouncementService service, String id) {
        return Objects.requireNonNull(service.getAnnouncementById(id).orElseThrow(NotFoundException::new));
    }
}
