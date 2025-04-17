package com.hublocal.board.handler.utils.UserUtils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserLogic {

    public static void validateUserIsAvailableByName(UserRepository repository, String name) {
        repository.findByUserName(name).ifPresent(user -> {
            throw new CustomException("User with name: '" + name + "' is already in the database");
        });
    }

}
