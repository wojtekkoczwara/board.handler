package com.hublocal.board.handler.controller;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.service.AnnouncementService;
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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AnnouncementController.ANNOUNCEMENT_PATH)
public class AnnouncementController {

    public static final String ANNOUNCEMENT_PATH = "/api/v1/announcements";
    public static final String ANNOUNCEMENT_ID = "/{id}";

    @Autowired
    private final AnnouncementService announcementService;

    @GetMapping
    public List<Announcement> findAll() {
        return announcementService.listAnnouncements();
    }

    @GetMapping(ANNOUNCEMENT_ID)
    public Announcement getById(@PathVariable String id) {
        return announcementService.getAnnouncementById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@Validated @RequestBody Announcement announcement) {

        Announcement announcement1 = announcementService.saveAnnouncement(announcement);
        String id = announcement1.getId().toString();
        return new ResponseEntity<>(getAnnouncement(announcementService, id), HttpStatus.CREATED);
    }

    @PutMapping(ANNOUNCEMENT_ID)
    public ResponseEntity<Announcement> updateById(@PathVariable String id, @Validated @RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(id, announcement));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(ANNOUNCEMENT_ID)
    public void deleteById(@PathVariable String id) {
        getAnnouncement(announcementService, id);
        announcementService.deleteAnnouncement(UUID.fromString(id));
    }

}
