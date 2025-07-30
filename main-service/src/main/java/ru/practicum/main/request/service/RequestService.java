package ru.practicum.main.request.service;

import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestStatusResultDto;
import ru.practicum.main.request.dto.RequestStatusUpdateDto;

import java.util.List;

public interface RequestService {
    RequestDto addRequest(Long userId, Long eventId);

    RequestDto updateRequestStatusToCanceledByUser(Long userId, Long requestId);

    List<RequestDto> getRequestsByUserId(Long userId);

    RequestStatusResultDto updateRequestsStatus(RequestStatusUpdateDto updateDto, Long userId, Long eventId);

    List<RequestDto> getRequestsByUserIdAndEventId(Long userId, Long eventId);
}
