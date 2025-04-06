package com.hublocal.board.handler.repository;

import com.hublocal.board.handler.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {
}
