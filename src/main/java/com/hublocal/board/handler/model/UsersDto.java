package com.hublocal.board.handler.model;

import com.hublocal.board.handler.entities.Announcement;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Builder
@Data
public class UsersDto {

    private UUID id;

    @Valid
    @Enumerated(EnumType.ORDINAL)
    private PermissionLevel permissionLevel;

    @NotBlank
    @Size(max = 50)
    private String userName;

    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    @Column(length = 100)
    private String company;

    @Size(max = 32)
    @Column(length = 32)
    private String commune;

    @Size(max = 32)
    @Column(length = 32)
    private String district;

    @Size(max = 9)
    @Column(length = 9)
    private String phoneNumber;

    public enum PermissionLevel {
        GUEST, USER, ADMIN, SUPER_ADMIN
    }
}
