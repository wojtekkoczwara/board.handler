package com.hublocal.board.handler.utils.UserUtils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.repository.UserRepository;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.UUID;
import java.util.stream.Collectors;

public class UserLogic {

    public static void validateUserIsAvailableByName(UserRepository repository, String name) {
        repository.findByUserName(name).ifPresent(user -> {
            throw new CustomException("User with name: '" + name + "' is already in the database");
        });
    }

    public static void verifyUserExist(UUID userId, UserRepository userRepository) {
        try {
            userRepository.findById(userId).orElseThrow(() -> new CustomException("User with id: '" + userId + "' not found"));
        } catch (HttpMessageNotReadableException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public static void verifyUserHasAnnouncement(String announcementId, String userId, UserRepository userRepository) {
        int size = userRepository.findById(UUID.fromString(userId)).orElseThrow(NotFoundException::new)
                .getAnnouncements().stream().filter(announcement -> announcement.getId().toString().equals(announcementId))
                .collect(Collectors.toSet()).size();

        if (size == 0) {
            throw new CustomException(String
                    .format("Announcement with id: '%s' does not belong to the user with id: '%s'", announcementId, userId));
        }
    }

}
