package com.hublocal.board.handler.utils.UserUtils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.repository.UserRepository;

public class UserLogic {

    public static void validateUserIsAvailableByName(UserRepository repository, String name) {
        repository.findByUserName(name).ifPresent(user -> {
            throw new CustomException("User with name: '" + name + "' is already in the database");
        });
    }

}
