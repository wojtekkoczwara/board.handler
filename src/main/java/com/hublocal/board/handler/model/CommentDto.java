package com.hublocal.board.handler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class CommentDto {

    private UUID id;

    @NotNull
    @Valid
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotNull
    @Size(max = 50, min = 2)
    private String commentTitle;

    @Size(max = 1000, min = 2)
    private String commentDescription;

    @JsonSerialize(as = LocalDateTime.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime createdDate;

    @JsonSerialize(as = LocalDateTime.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;


}
