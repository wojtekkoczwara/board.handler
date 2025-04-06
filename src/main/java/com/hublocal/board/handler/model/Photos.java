package com.hublocal.board.handler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Photos {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false, name = "photoId")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID photoId;

    @NotBlank
    private String photoName;

//    @ManyToOne
//    @JoinColumn(name = "id")
//    private Announcement announcement;


//    @ManyToOne
//    private Announcement announcement;
}
