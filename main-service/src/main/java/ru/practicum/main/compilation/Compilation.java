package ru.practicum.main.compilation;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.main.event.model.Event;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "compilations")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "title",
            nullable = false,
            unique = true
    )
    private String title;

    @Column(
            name = "pinned",
            nullable = false
    )
    private Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();
}
