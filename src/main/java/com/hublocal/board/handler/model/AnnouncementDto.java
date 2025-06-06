package com.hublocal.board.handler.model;

import com.hublocal.board.handler.entities.Photos;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class AnnouncementDto {
    private UUID id;

    @NotBlank
    @Size(max = 50)
    private String announcementName;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @Valid
    @NotNull
    private Integer categoryId;

    private String city;

    @Valid
    private Set<Photos> photos = new HashSet<>();

}
