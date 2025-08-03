package ru.practicum.main.rating;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.User;

import java.time.LocalDateTime;

import static ru.practicum.stats.client.StatClient.DATE_TIME_PATTERN;


@Entity
@Table(name = "ratings")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "rated",
            nullable = false
    )
    private Boolean rated;

    @Column(
            name = "created_date_time",
            nullable = false
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "evaluator_id",
            nullable = false
    )
    private User evaluator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "event_id",
            nullable = false
    )
    private Event event;
}
