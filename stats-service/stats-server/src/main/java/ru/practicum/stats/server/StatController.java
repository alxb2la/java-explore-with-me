package ru.practicum.stats.server;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.exception.ValidationException;
import ru.practicum.stats.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class StatController {
    private final StatService statService;

    @PostMapping(
            path = "/hit",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void addStat(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("StatController: Запрос на добавление Stat");
        statService.addStat(endpointHitDto);
        log.info("StatController: Успешно добавлен Stat");
    }

    @GetMapping(
            path = "/stats",
            produces = "application/json"
    )
    public List<ViewStatsDto> getViewStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           LocalDateTime start,
                                           @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") boolean unique) {
        log.info("StatController: Запрос на получение статистики");
        log.info("Параметры запроса. start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);
        if ((start == null) || (end == null) || (end.isBefore(start))) {
            throw new ValidationException("Uncorrected parameters start " + start + " & end " + end);
        }
        List<ViewStatsDto> viewStatsDtos = statService.getViewStats(start, end, uris, unique);
        log.info("StatController: Успешно получен список с объектами статистики размером: {}", viewStatsDtos.size());
        return viewStatsDtos;
    }
}
