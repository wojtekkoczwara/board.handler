package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> listUsers();

    Optional<User> getUserById(String id);

    User saveUser(User user);

    User updateUser(String id, User user);

    void deleteUser(UUID uuid);
}
