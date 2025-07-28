package ru.practicum.main.event.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Location {
    @Column(
            name = "lat",
            nullable = false
    )
    private Float lat;

    @Column(
            name = "lon",
            nullable = false
    )
    private Float lon;
}
