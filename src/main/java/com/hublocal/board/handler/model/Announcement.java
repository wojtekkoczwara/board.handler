package com.hublocal.board.handler.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Announcement {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false, name = "announcementId")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, updatable = false, nullable = false)
    private String announcementName;

    @NotBlank
    @Column(name = "announcement_description", length = 1000, updatable = false, nullable = false)
    private String description;

    @NotBlank
    @Column(name = "author", length = 50)
    private String author;

    @Column(length = 50)
    private String company;

    @Column(name = "category")
    private String category;

    @Column(length = 32)
    private String city;

    @Size(max = 32)
    @Column(length = 32)
    private String commune;

    @Column(length = 32)
    private String district;

    @Size(max = 9)
    @Column(length = 9)
    private String phoneNumber;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "announcementId", name = "announcementId")
    private Set<Photos> photos = new HashSet<>();
}
