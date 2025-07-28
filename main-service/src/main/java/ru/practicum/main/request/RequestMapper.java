package ru.practicum.main.request;

import ru.practicum.main.event.model.Event;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestStatus;
import ru.practicum.main.user.User;

import java.time.LocalDateTime;

public final class RequestMapper {

    private RequestMapper() {
        throw new UnsupportedOperationException();
    }

    public static Request toRequest(User requester, Event event, RequestStatus status) {
        return Request.builder()
                .id(null)
                .created(LocalDateTime.now())
                .status(status)
                .event(event)
                .requester(requester)
                .build();
    }

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .build();
    }
}
