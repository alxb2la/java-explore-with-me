package ru.practicum.main.rating.dto;

import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class RatingEventDto {
    private Long eventId;
    private Long like;
    private Long dislike;
}
