package ru.practicum.main.rating.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.rating.dto.RatingCreateDto;
import ru.practicum.main.rating.dto.RatingDto;
import ru.practicum.main.rating.service.RatingService;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/users",
        produces = "application/json"
)
public class RatingPrivateController {
    private final RatingService ratingService;

    @PostMapping(
            path = "/{userId}/events/{eventId}/rating",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RatingDto addRating(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId,
                               @RequestBody @Valid RatingCreateDto ratingCreateDto) {
        log.info("Запрос на добавление Rating: RatingCreateDto {}, userId {}, eventId {}", ratingCreateDto, userId, eventId);
        RatingDto ratingDto = ratingService.addRating(ratingCreateDto, userId, eventId);
        log.info("Успешно добавлен Rating: {}", ratingDto);
        return ratingDto;
    }

    @DeleteMapping(path = "/{userId}/events/{eventId}/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRating(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Запрос на удаление Rating: userId {}, eventId {}", userId, eventId);
        ratingService.removeRating(userId, eventId);
        log.info("Успешно удален Rating: userId {}, eventId {}", userId, eventId);
    }

    @GetMapping(path = "/{userId}/events/{eventId}/rating")
    @ResponseStatus(HttpStatus.OK)
    public RatingDto getRating(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId) {
        log.info("Запрос на получение Rating: userId {}, eventId {}", userId, eventId);
        RatingDto ratingDto = ratingService.getRating(userId, eventId);
        log.info("Успешно получен Rating: userId {}, eventId {}", userId, eventId);
        return ratingDto;
    }
}
