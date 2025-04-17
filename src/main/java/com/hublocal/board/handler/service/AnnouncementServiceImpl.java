package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Users;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.repository.CategoryRepository;
import com.hublocal.board.handler.repository.UserRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import com.hublocal.board.handler.utils.categoryUtils.CategoryLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import com.hublocal.board.handler.model.Announcement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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

    @Override
    @Transactional
    public Announcement saveAnnouncement(Announcement announcement, String userId) {
        UUID userUuid = UUID.fromString(userId);
        verifyUserExist(userUuid);

        verifyCategoryExist(announcement);
        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, announcement.getCategoryId())) {
            throw new CustomException("Category: '" + announcement.getCategoryId() + "' has children, category in the " +
                    "request must be lowest available level");
        }

        Announcement announcement1 = announcementRepository.saveAndFlush(announcement);
        Users user1 = userRepository.findById(userUuid).orElseThrow(NotFoundException::new);
        user1.getAnnouncements().add(announcement1);
        userRepository.saveAndFlush(user1);
        return announcement1;
    }


    @Override
    public Announcement updateAnnouncement(String id, Announcement announcement, String userId) {
        announcementUserVerify(id, userId);
        verifyCategoryExist(announcement);

        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, announcement.getCategoryId())) {
            throw new CustomException("Category: '" + announcement.getCategoryId() + "' has children, category in the " +
                    "request must be lowest available level");
        }

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
    public void deleteAnnouncement(String id, String userId) {
        announcementUserVerify(id, userId);
        announcementRepository.deleteById(UUID.fromString(id));
    }

    private void verifyCategoryExist(Announcement announcement) {
        try {
            categoryRepository.findById(announcement.getCategoryId()).orElseThrow(() -> new CustomException("Category with id: '" + announcement.getCategoryId() + "' not found"));
        } catch (HttpMessageNotReadableException e) {
            throw new CustomException(e.getMessage());
        }
    }

    private void announcementUserVerify(String id, String userId) {
        UUID userUuid = UUID.fromString(userId);
        verifyUserExist(userUuid);
        verifyUserHasAnnouncement(id, userId);
    }

    private void verifyUserExist(UUID userId) {
        try {
           userRepository.findById(userId).orElseThrow(() -> new CustomException("User with id: '" + userId + "' not found"));
        } catch (HttpMessageNotReadableException e) {
            throw new CustomException(e.getMessage());
        }
    }

    private void verifyUserHasAnnouncement(String id, String userId) {
        int size = userRepository.findById(UUID.fromString(userId)).orElseThrow(NotFoundException::new)
                .getAnnouncements().stream().filter(announcement -> announcement.getId().toString().equals(id))
                .collect(Collectors.toSet()).size();

        if(size == 0) {
            throw new CustomException(String
                    .format("Announcement with id: '%s' does not belong to the user with id: '%s'", id, userId));
        }
    }
}
