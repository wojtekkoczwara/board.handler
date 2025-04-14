package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.repository.CategoryRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import com.hublocal.board.handler.utils.categoryUtils.CategoryLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import com.hublocal.board.handler.model.Announcement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CategoryRepository categoryRepository;

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
        verifyCategoryExist(announcement);

        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, announcement.getCategoryId())) {
            throw new CustomException("Category: '" + announcement.getCategoryId() + "' has children, category in the " +
                    "request must be lowest available level");
        }
        return announcementRepository.save(announcement);
    }

    @Override
    public Announcement updateAnnouncement(String id, Announcement announcement) {
        verifyCategoryExist(announcement);
        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, announcement.getCategoryId())) {
            throw new CustomException("Category: '" + announcement.getCategoryId() + "' has children, category in the " +
                    "request must be lowest available level");
        }
        Announcement announcementDb;
        try {
            announcementDb = HandleFoundObject.getAnnouncement(this, id);
            verifyCategoryExist(announcement);
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

    private void verifyCategoryExist(Announcement announcement) {
        try {
            categoryRepository.findById(announcement.getCategoryId()).orElseThrow(() -> new CustomException("Category with id: '" + announcement.getCategoryId() + "' not found"));
        } catch (HttpMessageNotReadableException e) {
            throw new CustomException(e.getMessage());
        }
    }
}
