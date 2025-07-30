package ru.practicum.main.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventGetParamsDto;
import ru.practicum.main.event.dto.EventUpdateAdminReqDto;
import ru.practicum.main.event.service.EventService;

import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/admin/events",
        produces = "application/json"
)
public class EventAdminController {
    private final EventService eventService;

    @PatchMapping(
            path = "/{eventId}",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable @NotNull Long eventId,
                                           @RequestBody @Valid EventUpdateAdminReqDto eventDto) {
        log.info("Admin: Запрос на обновление Event с id {}", eventId);
        EventFullDto eventFullDto = eventService.updateEventByAdmin(eventDto, eventId);
        log.info("Admin: Успешно обновлен Event: {}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventFullDto> getEventsByAdmin(@ModelAttribute EventGetParamsDto eventParamsDto) {
        log.info("Admin: Запрос на получение списка Events: params {}", eventParamsDto);
        Collection<EventFullDto> eventDtos = eventService.getEventsByAdmin(eventParamsDto);
        log.info("Admin: Успешно сформирован список Events размером {}", eventDtos.size());
        return eventDtos;
    }
}
