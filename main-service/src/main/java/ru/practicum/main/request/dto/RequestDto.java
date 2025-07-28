package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.request.model.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.stats.client.StatClient.DATE_TIME_PATTERN;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
@ToString
public class RequestDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;

    private RequestStatus status;
    private Long event;
    private Long requester;
}
