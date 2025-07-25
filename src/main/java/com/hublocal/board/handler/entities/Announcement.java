package com.hublocal.board.handler.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(length = 50, updatable = true, nullable = false)
    private String announcementName;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "announcement_description", length = 1000, updatable = false, nullable = false)
    private String description;

    @Valid
    @NotNull
    private Integer categoryId;

    @Size(max = 32)
    @Column(length = 32)
    private String city;

    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "announcementId", name = "announcementId")
    private Set<Photos> photos = new HashSet<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "announcementId")
    private Set<Comment> comments = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
}
