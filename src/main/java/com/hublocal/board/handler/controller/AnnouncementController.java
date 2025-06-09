package com.hublocal.board.handler.controller;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.model.AnnouncementDto;
import com.hublocal.board.handler.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hublocal.board.handler.utils.HandleFoundObject.getAnnouncement;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AnnouncementController.ANNOUNCEMENT_PATH)
public class AnnouncementController {

    public static final String ANNOUNCEMENT_PATH = "/api/v1/announcements";
    public static final String ANNOUNCEMENT_ID = "/{announcementId}";

    private final AnnouncementService announcementService;

    @GetMapping
    public List<AnnouncementDto> findAll() {
        return announcementService.listAnnouncements();
    }

    @GetMapping(ANNOUNCEMENT_ID)
    public AnnouncementDto getById(@PathVariable String announcementId) {
        return announcementService.getAnnouncementById(announcementId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<AnnouncementDto> createAnnouncement(@Validated @RequestBody AnnouncementDto announcementDto, @RequestHeader("userId") String userId) {

        AnnouncementDto announcement1 = announcementService.saveAnnouncement(announcementDto, userId);
        String id = announcement1.getId().toString();
        return new ResponseEntity<>(getAnnouncement(announcementService, id), HttpStatus.CREATED);
    }

    @PutMapping(ANNOUNCEMENT_ID)
    public ResponseEntity<AnnouncementDto> updateById(@PathVariable String announcementId, @Validated @RequestBody AnnouncementDto announcementDto, @RequestHeader("userId") String userId) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(announcementId, announcementDto, userId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(ANNOUNCEMENT_ID)
    public void deleteById(@PathVariable String announcementId,  @RequestHeader("userId") String userId) {
        getAnnouncement(announcementService, announcementId);
        announcementService.deleteAnnouncement(announcementId, userId);
    }
}
