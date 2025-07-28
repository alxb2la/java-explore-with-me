package ru.practicum.main.event.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<EventState> from(String checkedParam) {
        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(checkedParam))
                .findFirst();
    }
}
