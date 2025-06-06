package com.hublocal.board.handler.model;

import com.hublocal.board.handler.entities.Announcement;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Builder
@Data
public class CategoryDto {

    private Integer id;

    @Valid
    private Integer parentId;

    @NotBlank
    @Size(max = 32)
    private String name;
}
