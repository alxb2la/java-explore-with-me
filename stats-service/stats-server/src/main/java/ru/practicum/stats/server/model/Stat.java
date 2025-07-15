package ru.practicum.stats.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "stats")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "app",
            nullable = false
    )
    private String app;

    @Column(
            name = "uri",
            nullable = false
    )
    private String uri;

    @Column(
            name = "ip",
            nullable = false
    )
    private String ip;

    @Column(
            name = "created_date_time",
            nullable = false
    )
    private LocalDateTime timestamp;
}
