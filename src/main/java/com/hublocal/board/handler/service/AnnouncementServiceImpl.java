package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.entities.Users;
import com.hublocal.board.handler.mappers.AnnouncementMapper;
import com.hublocal.board.handler.model.AnnouncementDto;
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
import java.util.stream.Collectors;

import static com.hublocal.board.handler.utils.UserUtils.UserLogic.verifyUserExist;
import static com.hublocal.board.handler.utils.announcementUtils.AnnouncementLogic.announcementUserVerify;
import static com.hublocal.board.handler.utils.categoryUtils.CategoryLogic.verifyCategoryExist;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementMapper announcementMapper;

    private final AnnouncementRepository announcementRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<AnnouncementDto> listAnnouncements() {
        return announcementRepository.findAll().stream().map(announcementMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<AnnouncementDto> getAnnouncementById(String id) {
        try {
            return announcementRepository.findById(UUID.fromString(id)).map(announcementMapper::toDto);
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
    }

    @Override
    @Transactional
    public AnnouncementDto saveAnnouncement(AnnouncementDto announcementDto, String userId) {
        UUID userUuid = UUID.fromString(userId);
        verifyUserExist(userUuid, userRepository);

        verifyCategoryExist(announcementDto, categoryRepository);
        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, announcementDto.getCategoryId())) {
            throw new CustomException("Category: '" + announcementDto.getCategoryId() + "' has children, category in the " +
                    "request must be lowest available level");
        }

        Announcement announcement1 = announcementRepository.saveAndFlush(announcementMapper.toEntity(announcementDto));
        Users user1 = userRepository.findById(userUuid).orElseThrow(NotFoundException::new);
        user1.getAnnouncements().add(announcement1);
        userRepository.saveAndFlush(user1);
        return announcementMapper.toDto(announcement1);
    }


    @Override
    public AnnouncementDto updateAnnouncement(String id, AnnouncementDto announcementDto, String userId) {
        announcementUserVerify(id, userId, userRepository);
        verifyCategoryExist(announcementDto, categoryRepository);

        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, announcementDto.getCategoryId())) {
            throw new CustomException("Category: '" + announcementDto.getCategoryId() + "' has children, category in the " +
                    "request must be lowest available level");
        }

        Announcement announcementDb;

        try {
            announcementDb = HandleFoundObject.getAnnouncementFromRepository(announcementRepository, UUID.fromString(id));

            announcementDto.setId(announcementDb.getId());
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }

        return announcementMapper.toDto(announcementRepository.saveAndFlush(announcementDb));
    }

    @Override
    public void deleteAnnouncement(String id, String userId) {
        announcementUserVerify(id, userId, userRepository);
        announcementRepository.deleteById(UUID.fromString(id));
    }


}
