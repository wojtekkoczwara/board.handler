package com.hublocal.board.handler.utils.announcementUtils;

import com.hublocal.board.handler.repository.UserRepository;

import java.util.UUID;

import static com.hublocal.board.handler.utils.UserUtils.UserLogic.verifyUserExist;
import static com.hublocal.board.handler.utils.UserUtils.UserLogic.verifyUserHasAnnouncement;

public class AnnouncementLogic {

    public static void announcementUserVerify(String announcementId, String userId, UserRepository repository) {
        UUID userUuid = UUID.fromString(userId);
        verifyUserExist(userUuid, repository);
        verifyUserHasAnnouncement(announcementId, userId, repository);
    }
}
