package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.model.Users;
import com.hublocal.board.handler.repository.UserRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import com.hublocal.board.handler.utils.UserUtils.UserLogic;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<Users> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> getUserById(String id) {
        try {
            return userRepository.findById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
    }

    @Override
    public Users saveUser(Users user) {
        UserLogic.validateUserIsAvailableByName(userRepository, user.getUserName());
        return userRepository.save(user);
    }

    @Override
    public Users updateUser(String id, Users user) {
        Users userDb;

        try {
            userDb = HandleFoundObject.getUser(this, id);
            if(!userDb.getUserName().equals(user.getUserName())) {
                UserLogic.validateUserIsAvailableByName(userRepository, user.getUserName());
            }
            user.setId(userDb.getId());
            if(user.getAnnouncements().isEmpty()) {
                user.getAnnouncements().addAll(userDb.getAnnouncements());
            }
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }
}
