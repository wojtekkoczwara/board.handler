package com.hublocal.board.handler.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 3, updatable = false, nullable = false, name = "categoryId")
    private Integer id;

    @Valid
    private Integer parentId;

    @NotBlank
    @Size(max = 32)
    @Column(length = 32, unique = true)
    private String name;

    @OneToMany(orphanRemoval = false)
    @JoinColumn(referencedColumnName = "categoryId", name = "categoryId")
    private Set<Announcement> announcementSet  = new HashSet<>();

}
