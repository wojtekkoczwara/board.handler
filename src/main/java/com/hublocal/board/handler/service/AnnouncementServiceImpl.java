package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.model.Users;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.repository.CategoryRepository;
import com.hublocal.board.handler.repository.UserRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import com.hublocal.board.handler.utils.categoryUtils.CategoryLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hublocal.board.handler.utils.UserUtils.UserLogic.verifyUserExist;
import static com.hublocal.board.handler.utils.announcementUtils.AnnouncementLogic.announcementUserVerify;
import static com.hublocal.board.handler.utils.categoryUtils.CategoryLogic.verifyCategoryExist;

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
        verifyUserExist(userUuid, userRepository);

        verifyCategoryExist(announcement, categoryRepository);
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
        announcementUserVerify(id, userId, userRepository);
        verifyCategoryExist(announcement, categoryRepository);

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
        announcementUserVerify(id, userId, userRepository);
        announcementRepository.deleteById(UUID.fromString(id));
    }


}
