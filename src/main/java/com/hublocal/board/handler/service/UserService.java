package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.Users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<Users> listUsers();

    Optional<Users> getUserById(String id);

    Users saveUser(Users user);

    Users updateUser(String id, Users user);

    void deleteUser(UUID uuid);
}
