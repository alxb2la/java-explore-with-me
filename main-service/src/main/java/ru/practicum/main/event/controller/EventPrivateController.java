package ru.practicum.main.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.service.EventService;

import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/users",
        produces = "application/json"
)
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping(
            path = "/{userId}/events",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @NotNull Long userId,
                                 @RequestBody @Valid EventCreateDto eventCreateDto) {
        log.info("Запрос на добавление Event: {}", eventCreateDto);
        EventFullDto eventFullDto = eventService.addEvent(eventCreateDto, userId);
        log.info("Успешно добавлен Event: {}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping(
            path = "/{userId}/events/{eventId}",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByInitiator(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId,
                                               @RequestBody @Valid EventUpdateUserReqDto eventDto) {
        log.info("Запрос на обновление Event с id {}, User c id {}", eventId, userId);
        EventFullDto eventFullDto = eventService.updateEventByInitiator(eventDto, userId, eventId);
        log.info("Успешно обновлен Event: {}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping(path = "/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getEventsByInitiatorId(
            @PathVariable @NotNull Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение списка Events: from {}, size {}, userId {}", from, size, userId);
        Collection<EventShortDto> eventDtos = eventService.getEventsByInitiatorId(userId, from, size);
        log.info("Успешно сформирован список Events размером {}", eventDtos.size());
        return eventDtos;
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventPrivateFullDto getEventByInitiatorIdAndEventId(@PathVariable @NotNull Long userId,
                                                               @PathVariable @NotNull Long eventId) {
        log.info("Запрос на получение Event с id {}, User c id {}", eventId, userId);
        EventPrivateFullDto eventDto = eventService.getEventByInitiatorIdAndEventId(userId, eventId);
        log.info("Успешно получен Event: {}", eventDto);
        return eventDto;
    }
}
