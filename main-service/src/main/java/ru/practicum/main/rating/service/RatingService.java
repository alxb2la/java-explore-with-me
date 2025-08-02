package ru.practicum.main.rating.service;

import ru.practicum.main.rating.dto.RatingCreateDto;
import ru.practicum.main.rating.dto.RatingDto;
import ru.practicum.main.rating.dto.RatingEventDto;

import java.util.List;

public interface RatingService {
    RatingDto addRating(RatingCreateDto ratingCreateDto, Long userId, Long eventId);

    void removeRating(Long userId, Long eventId);

    RatingDto getRating(Long userId, Long eventId);

    RatingEventDto getRatingOfEvent(Long eventId);

    List<RatingEventDto> getTopRatingsOfEvents(Integer from, Integer size);
}
