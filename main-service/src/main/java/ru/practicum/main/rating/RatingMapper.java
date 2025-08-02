package ru.practicum.main.rating;

import ru.practicum.main.event.model.Event;
import ru.practicum.main.rating.dto.RatingCreateDto;
import ru.practicum.main.rating.dto.RatingDto;
import ru.practicum.main.user.User;

import java.time.LocalDateTime;

public final class RatingMapper {

    private RatingMapper() {
        throw new UnsupportedOperationException();
    }

    public static Rating toRating(RatingCreateDto ratingCreateDto, User evaluator, Event event) {
        return Rating.builder()
                .id(null)
                .rated(ratingCreateDto.getRated())
                .created(LocalDateTime.now())
                .evaluator(evaluator)
                .event(event)
                .build();
    }

    public static RatingDto toRatingDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .rated(rating.getRated())
                .created(rating.getCreated())
                .evaluatorId(rating.getEvaluator().getId())
                .eventId(rating.getEvent().getId())
                .build();
    }
}
