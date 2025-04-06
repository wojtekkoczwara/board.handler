package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.hublocal.board.handler.model.Announcement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Override
    public List<Announcement> listAnnouncements() {
        return announcementRepository.findAll();
    }

    @Override
    public Optional<Announcement> getAnnouncementById(String id) {
        try {
            return announcementRepository.findById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
           throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
    }

    @Transactional
    @Override
    public Announcement saveAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Override
    public Announcement updateAnnouncement(String id, Announcement announcement) {
        Announcement announcementDb;
        try {
            announcementDb = HandleFoundObject.getAnnouncement(this, id);
            announcement.setId(announcementDb.getId());
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
        return announcementRepository.saveAndFlush(announcement);
    }

    @Override
    public void deleteAnnouncement(UUID id) {
        announcementRepository.deleteById(id);
    }
}
