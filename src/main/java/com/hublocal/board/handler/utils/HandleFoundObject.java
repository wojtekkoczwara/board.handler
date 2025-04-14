package com.hublocal.board.handler.utils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.model.Category;
import com.hublocal.board.handler.model.User;
import com.hublocal.board.handler.service.AnnouncementService;
import com.hublocal.board.handler.service.CategoryService;
import com.hublocal.board.handler.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.Objects;

public class HandleFoundObject {

    public static Announcement getAnnouncement(AnnouncementService service, String id) {
        return Objects.requireNonNull(service.getAnnouncementById(id).orElseThrow(NotFoundException::new));
    }

    public static Category getCategory(CategoryService service, int id) {
        return Objects.requireNonNull(service.getCategoryById(id).orElseThrow(NotFoundException::new));
    }

    public static User getUser(UserService service, String id) {
        return Objects.requireNonNull(service.getUserById(id).orElseThrow(NotFoundException::new));
    }

    public static Category getParentCategory(CategoryService service, Integer parentId) {
        return service.getCategoryById(parentId)
                .orElseThrow(() -> new CustomException("Parent id: '" + parentId + "' not found"));
    }
}
