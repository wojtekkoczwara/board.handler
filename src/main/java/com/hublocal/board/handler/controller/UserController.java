package com.hublocal.board.handler.controller;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.entities.Users;
import com.hublocal.board.handler.model.AnnouncementDto;
import com.hublocal.board.handler.model.UsersDto;
import com.hublocal.board.handler.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.hublocal.board.handler.utils.HandleFoundObject.getUser;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(UserController.USERS_PATH)
public class UserController {

    public static final String USERS_PATH = "/api/v1/users";
    public static final String USER_ID = "/{id}";
    public static final String USER_ANNOUNCEMENTS = USER_ID + "/announcements";

    private final UserService userService;

    @GetMapping
    public List<UsersDto> findAll() {
        return userService.listUsers();
    }

    @GetMapping(USER_ID)
    public UsersDto getById(@PathVariable String id) {
        return userService.getUserById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping(USER_ANNOUNCEMENTS)
    public List<String> getUserAnnouncementsIdByUserId(@PathVariable String id){
        return userService.getUserAnnouncementByUserId(id);
    }

    @PostMapping
    public ResponseEntity<UsersDto> createUser(@Validated @RequestBody UsersDto user) {

        UsersDto user1 = userService.saveUser(user);
        String id = user1.getId().toString();
        return new ResponseEntity<>(getUser(userService, id), HttpStatus.CREATED);
    }

    @PutMapping(USER_ID)
    public ResponseEntity<UsersDto> updateById(@PathVariable String id, @Validated @RequestBody UsersDto user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(USER_ID)
    public void deleteById(@PathVariable String id) {
        getUser(userService, id);
        userService.deleteUser(UUID.fromString(id));
    }

}
