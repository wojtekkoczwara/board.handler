package com.hublocal.board.handler.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.annotations.CascadeType.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false, name = "userId")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Valid
    @Enumerated(EnumType.ORDINAL)
    private PermissionLevel permissionLevel;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false, unique = true)
    private String userName;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(name = "firstName", length = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "lastName", length = 100)
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "userId", name = "userId")
    private Set<Announcement> announcements = new HashSet<>();

    private enum PermissionLevel {
        GUEST, USER, ADMIN, SUPER_ADMIN
    }
}
