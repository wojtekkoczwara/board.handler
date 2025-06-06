package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.entities.Users;
import com.hublocal.board.handler.mappers.AnnouncementMapper;
import com.hublocal.board.handler.mappers.UsersMapper;
import com.hublocal.board.handler.model.UsersDto;
import com.hublocal.board.handler.repository.UserRepository;
import com.hublocal.board.handler.utils.UserUtils.UserLogic;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hublocal.board.handler.utils.HandleFoundObject.getUserFromRepository;
import static com.hublocal.board.handler.utils.UserUtils.UserLogic.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final AnnouncementMapper announcementMapper;

    @Override
    public List<UsersDto> listUsers() {
        return userRepository.findAll().stream().map(usersMapper::toDto).toList();
    }

    @Override
    public Optional<UsersDto> getUserById(String id) {
        try {
            return userRepository.findById(UUID.fromString(id)).map(usersMapper::toDto);
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
    }

    @Override
    public UsersDto saveUser(UsersDto usersDto) {
        validateUserName(usersDto.getUserName());

        verifyEmailCorrect(userRepository, usersDto.getEmail());
        validateUserIsAvailableByName(userRepository, usersDto.getUserName());
        Users userEntity = usersMapper.toEntity(usersDto);
        return usersMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public UsersDto updateUser(String id, UsersDto userDto) {
        validateUserName(userDto.getUserName());

        verifyEmailCorrect(userRepository, userDto.getEmail());
        Users userDb;
        try {
            userDb = getUserFromRepository(userRepository, id);
            if(!userDb.getUserName().equals(userDto.getUserName())) {
                validateUserIsAvailableByName(userRepository, userDto.getUserName());
            }

            Users userToSave = usersMapper.toEntity(userDto);
            userToSave.setId(userDb.getId());

            userToSave.setAnnouncements(userDb.getAnnouncements());
            return usersMapper.toDto(userRepository.saveAndFlush(userToSave));
        } catch (IllegalArgumentException e) {
            throw new CustomException("UUID: '" + id + "' must be a valid UUID");
        }
    }

    @Override
    @Transactional
    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public List<String> getUserAnnouncementByUserId(String id) {
        return getUserFromRepository(userRepository, id).getAnnouncements().stream()
                .map(announcement -> announcement.getId().toString()).toList();
    }
}
