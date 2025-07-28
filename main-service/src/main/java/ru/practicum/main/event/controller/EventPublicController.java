package ru.practicum.main.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventGetPublicParamsDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.service.EventService;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/events",
        produces = "application/json"
)
public class EventPublicController {
    private final EventService eventService;
    private final StatClient statClient;
    @Value("${app.name}")
    private String applicationName;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getPublicEvents(@ModelAttribute EventGetPublicParamsDto eventParamsDto,
                                                     HttpServletRequest request) {
        log.info("Public: Запрос на получение списка Events: params {}", eventParamsDto);
        Collection<EventShortDto> eventDtos = eventService.getPublicEvents(eventParamsDto);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(applicationName)
                .uri("/events")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.addStat(endpointHitDto);

        log.info("Public: Успешно сформирован список Events размером {}", eventDtos.size());
        return eventDtos;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable @NotNull Long id, HttpServletRequest request) {
        log.info("Public: Запрос на получение Event оп id: {}", id);
        EventFullDto event = eventService.getPublicEventById(id);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(applicationName)
                .uri("/events/" + id)
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.addStat(endpointHitDto);

        log.info("Public: Успешно получен Event {}", event);
        return event;
    }
}
