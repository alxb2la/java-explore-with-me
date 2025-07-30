package ru.practicum.main.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.main.category.Category;
import ru.practicum.main.user.User;

import java.time.LocalDateTime;

import static ru.practicum.stats.client.StatClient.DATE_TIME_PATTERN;


@Entity
@Table(name = "events")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "annotation",
            nullable = false
    )
    private String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    @Column(
            name = "confirmed_requests",
            nullable = false
    )
    private Long confirmedRequests;

    @Column(
            name = "created_date_time",
            nullable = false
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(
            name = "event_start_date_time",
            nullable = false
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "initiator_id",
            nullable = false
    )
    private User initiator;

    @Embedded
    private Location location;

    @Column(
            name = "paid",
            nullable = false
    )
    private Boolean paid;

    @Column(
            name = "participant_limit",
            nullable = false
    )
    private Integer participantLimit;

    @Column(name = "published_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    @Column(
            name = "request_moderation",
            nullable = false
    )
    private Boolean requestModeration;

    @Column(
            name = "state",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(
            name = "title",
            nullable = false
    )
    private String title;
}
