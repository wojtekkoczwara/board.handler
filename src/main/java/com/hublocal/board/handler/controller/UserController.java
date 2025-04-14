package com.hublocal.board.handler.controller;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.model.User;
import com.hublocal.board.handler.service.AnnouncementService;
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

import static com.hublocal.board.handler.utils.HandleFoundObject.getAnnouncement;
import static com.hublocal.board.handler.utils.HandleFoundObject.getUser;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(UserController.USERS_PATH)
public class UserController {

    public static final String USERS_PATH = "/api/v1/users";
    public static final String USER_ID = "/{id}";

    @Autowired
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.listUsers();
    }

    @GetMapping(USER_ID)
    public User getById(@PathVariable String id) {
        return userService.getUserById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<User> createAnnouncement(@Validated @RequestBody User user) {

        User user1 = userService.saveUser(user);
        String id = user1.getId().toString();
        return new ResponseEntity<>(getUser(userService, id), HttpStatus.CREATED);
    }

    @PutMapping(USER_ID)
    public ResponseEntity<User> updateById(@PathVariable String id, @Validated @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(USER_ID)
    public void deleteById(@PathVariable String id) {
        getUser(userService, id);
        userService.deleteUser(UUID.fromString(id));
    }

}
