package ru.practicum.main.event.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.client.StatClient.DATE_TIME_PATTERN;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EventGetPublicParamsDto {
    private String text;
    private List<Long> categories;
    private Boolean paid;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime rangeStart;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable = false;
    private EventSortingType sort;
    private Integer from = 0;
    private Integer size = 10;
}
