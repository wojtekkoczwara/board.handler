package com.hublocal.board.handler.utils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.entities.Category;
import com.hublocal.board.handler.entities.Users;
import com.hublocal.board.handler.model.AnnouncementDto;
import com.hublocal.board.handler.model.CategoryDto;
import com.hublocal.board.handler.model.UsersDto;
import com.hublocal.board.handler.repository.AnnouncementRepository;
import com.hublocal.board.handler.repository.CategoryRepository;
import com.hublocal.board.handler.repository.UserRepository;
import com.hublocal.board.handler.service.AnnouncementService;
import com.hublocal.board.handler.service.CategoryService;
import com.hublocal.board.handler.service.UserService;

import java.util.Objects;
import java.util.UUID;

public class HandleFoundObject {

    public static AnnouncementDto getAnnouncement(AnnouncementService service, String id) {
        return Objects.requireNonNull(service.getAnnouncementById(id).orElseThrow(NotFoundException::new));
    }

    public static Announcement getAnnouncementFromRepository(AnnouncementRepository repository, UUID id) {
        return Objects.requireNonNull(repository.findById(id).orElseThrow(NotFoundException::new));
    }

    public static CategoryDto getCategory(CategoryService service, int id) {
        return Objects.requireNonNull(service.getCategoryById(id).orElseThrow(NotFoundException::new));
    }
    public static Category getCategoryFromRepository(CategoryRepository repository, int id) {
        return Objects.requireNonNull(repository.findById(id).orElseThrow(NotFoundException::new));
    }

    public static UsersDto getUser(UserService service, String id) {
        return Objects.requireNonNull(service.getUserById(id).orElseThrow(NotFoundException::new));
    }

    public static Users getUserFromRepository(UserRepository repository, String id) {
        return Objects.requireNonNull(repository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new));
    }

    public static CategoryDto getParentCategory(CategoryService service, Integer parentId) {
        return service.getCategoryById(parentId)
                .orElseThrow(() -> new CustomException("Parent id: '" + parentId + "' not found"));
    }
}
