package ru.practicum.main.event.dto;

import java.util.Optional;
import java.util.stream.Stream;

public enum EventStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static Optional<EventStateAction> from(String checkedParam) {
        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(checkedParam))
                .findFirst();
    }
}
