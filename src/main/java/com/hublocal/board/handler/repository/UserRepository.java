package com.hublocal.board.handler.repository;

import com.hublocal.board.handler.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUserName(String name);

}
