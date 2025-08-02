package ru.practicum.main.rating.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.rating.dto.RatingEventDto;
import ru.practicum.main.rating.service.RatingService;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/events",
        produces = "application/json"
)
public class RatingPublicController {
    private final RatingService ratingService;

    @GetMapping("{eventId}/rating")
    @ResponseStatus(HttpStatus.OK)
    public RatingEventDto getRatingOfEvent(@PathVariable Long eventId) {
        log.info("Public: Запрос на получение Rating Of Event, Event id {}", eventId);
        RatingEventDto ratingEventDto = ratingService.getRatingOfEvent(eventId);
        log.info("Public: Успешно получен Rating Of Event, {}", eventId);
        return ratingEventDto;
    }

    @GetMapping("/rating/top")
    public List<RatingEventDto> getTopRatingsOfEvents(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Public: Запрос на получение Top Ratings Of Events, from {}, size {}", from, size);
        List<RatingEventDto> ratingEventDtos = ratingService.getTopRatingsOfEvents(from, size);
        log.info("Успешно сформирован список Ratings размером {}", ratingEventDtos.size());
        return ratingEventDtos;
    }
}
