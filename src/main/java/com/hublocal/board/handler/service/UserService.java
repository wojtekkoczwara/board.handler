package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.UsersDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UsersDto> listUsers();

    Optional<UsersDto> getUserById(String id);

    UsersDto saveUser(UsersDto usersDto);

    UsersDto updateUser(String id, UsersDto userDto);

    void deleteUser(UUID uuid);

    List<String> getUserAnnouncementByUserId(String id);
}
