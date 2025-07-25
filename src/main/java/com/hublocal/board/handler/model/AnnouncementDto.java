package com.hublocal.board.handler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hublocal.board.handler.entities.Photos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
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

    @JsonSerialize(as = LocalDateTime.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime createdDate;

    @JsonSerialize(as = LocalDateTime.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;

}
